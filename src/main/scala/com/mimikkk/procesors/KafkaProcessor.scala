package com.mimikkk.procesors

import com.mimikkk.models.stockprice.anomaly.{StockPriceAnomalyAggregator, StockPriceAnomalyEntitleFunction, StockPriceAnomalyProcessFunction}
import com.mimikkk.models.stockprice.record.{StockPriceRecordAggregator, StockPriceRecordEntitleFunction, StockPriceRecordProcessFunction}
import com.mimikkk.models.stockprice.{StockMeta, StockPrice, StockPriceWatermarkStrategy}
import com.mimikkk.sinks.{DatabaseSinkFactory, KafkaSinkFactory}
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.restartstrategy.RestartStrategies.fixedDelayRestart
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.connector.jdbc.JdbcStatementBuilder
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger

import java.sql.PreparedStatement

object KafkaProcessor extends Processor {
  if (args.length != 11) {
    println(
      s"""
         |USAGE of Processor:
         |  <dataset-metafile-path: string>
         |  <kafka-server: server-string>
         |  <kafka-group-id: string>
         |  <kafka-content-topic: string>
         |  <kafka-anomaly-topic: string>
         |  <database-url: url-string>
         |  <database-username: string>
         |  <database-password: string>
         |  <anomaly-day-range: day-count>
         |  <anomaly-percentage-fluctuation: int-percentage>
         |  <update-strategy: One of '${UpdateStrategy.values mkString ", "}'>
        """.stripMargin
    )
    System.exit(1)
  }

  private final object configuration {
    val meta: String = args(0)

    final object kafka {
      val server: String = args(1)
      val groupId: String = args(2)
      val contentTopic: String = args(3)
      val anomalyTopic: String = args(4)
    }

    final object database {
      val url: String = args(5)
      val username: String = args(6)
      val password: String = args(7)
    }

    final object anomaly {
      val dayRange: Int = args(8).toInt
      val percentageFluctuation: Float = args(9).toFloat / 100
    }

    val updateStrategy: UpdateStrategy.Type = UpdateStrategy.from(args(10))
  }


  private final val numberOfRetries = 5
  private final val millisecondsBetweenAttempts = 5

  private final val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

  private final val environment = StreamExecutionEnvironment.getExecutionEnvironment
  environment.getConfig.setRestartStrategy(fixedDelayRestart(numberOfRetries, millisecondsBetweenAttempts))
  environment.registerCachedFile(configuration.meta, StockMeta.name)

  private final val source = KafkaSource.builder[String]
    .setBootstrapServers(configuration.kafka.server)
    .setTopics(configuration.kafka.contentTopic)
    .setGroupId(configuration.kafka.groupId)
    .setStartingOffsets(OffsetsInitializer.earliest)
    .setValueOnlyDeserializer(new SimpleStringSchema)
    .build

  private final val stringStream = environment fromSource
    (source, WatermarkStrategy.noWatermarks(), s"Kafka ${configuration.kafka.contentTopic} Source")

  private final val recordStream = stringStream
    .map(_ split ",")
    .map(stream => StockPrice(
      format parse stream(0),
      stream(1).toFloat,
      stream(2).toFloat,
      stream(3).toFloat,
      stream(4).toFloat,
      stream(5).toFloat,
      stream(6).toInt,
      stream(7),
    ))
    .assignTimestampsAndWatermarks(StockPriceWatermarkStrategy.create())

  private final val url = configuration.database.url
  private final val username = configuration.database.username
  private final val password = configuration.database.password


  private final val insertStatement: String =
    """
    INSERT INTO stock_prices (
      window_start,
      stock_id,
      title,
      close,
      low,
      high,
      volume
    ) VALUES (?, ?, ?, ?, ?, ?, ?)
      ON DUPLICATE KEY UPDATE close=?, low=?, high=?, volume=?
    """

  private val every = configuration.updateStrategy match {
    case UpdateStrategy.Realtime => Time days 1
    case UpdateStrategy.Historical => Time seconds 10
  }

  recordStream
    .keyBy(_.stockId)
    .window(TumblingEventTimeWindows of (Time days 30))
    .aggregate(new StockPriceRecordAggregator, new StockPriceRecordProcessFunction)
    .map(new StockPriceRecordEntitleFunction)
    .addSink(DatabaseSinkFactory.create[StockPriceRecordEntitleFunction.Result](
      insertStatement,
      // Has to be verbose to ensure serialization for Spark preprocessor
      new JdbcStatementBuilder[StockPriceRecordEntitleFunction.Result] {
        override def accept(statement: PreparedStatement, price: StockPriceRecordEntitleFunction.Result): Unit = {
          statement.setLong(1, price.start)
          statement.setString(2, price.stockId)
          statement.setString(3, price.title)
          statement.setFloat(4, price.close)
          statement.setFloat(5, price.low)
          statement.setFloat(6, price.high)
          statement.setFloat(7, price.volume)
          statement.setFloat(8, price.close)
          statement.setFloat(9, price.low)
          statement.setFloat(10, price.high)
          statement.setFloat(11, price.volume)
        }
      },
      url,
      username,
      password
    ))


  private final val percentageFluctuation = configuration.anomaly.percentageFluctuation
  recordStream
    .keyBy(_.stockId)
    .window(TumblingEventTimeWindows of (Time days configuration.anomaly.dayRange))
    .aggregate(new StockPriceAnomalyAggregator, new StockPriceAnomalyProcessFunction)
    .map(new StockPriceAnomalyEntitleFunction)
    .filter(_.fluctuation > percentageFluctuation)
    .map(_.toString)
    .sinkTo(KafkaSinkFactory.create(configuration.kafka.server, configuration.kafka.anomalyTopic))

  environment.execute("Stock prices processing...")
}
