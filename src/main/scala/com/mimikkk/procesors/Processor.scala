package com.mimikkk.procesors

import com.mimikkk.models.stockprice.anomaly.{StockPriceAnomalyAggregator, StockPriceAnomalyProcessFunction}
import com.mimikkk.models.stockprice.record.{StockPriceRecordAggregator, StockPriceRecordProcessFunction}
import com.mimikkk.models.stockprice.{StockPrice, StockPriceWatermarkStrategy}
import com.mimikkk.sinks.{DatabaseSinkFactory, KafkaSinkFactory}
import org.apache.flink.api.common.restartstrategy.RestartStrategies._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.connector.jdbc.JdbcStatementBuilder
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.sql.PreparedStatement
import java.util.Properties
import scala.collection.immutable.HashMap.EmptyHashMap.split

object Processor {
  def main(args: Array[String]): Unit = {
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

    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

    def intoStockPrice = (stream: Array[String]) => StockPrice(
      format parse stream(0),
      stream(1).toFloat,
      stream(2).toFloat,
      stream(3).toFloat,
      stream(4).toFloat,
      stream(5).toFloat,
      stream(6).toInt,
      stream(7),
    )

    val meta = args(0)
    val server = args(1)
    val groupId = args(2)
    val contentTopic = args(3)
    val anomalyTopic = args(4)
    val url = args(5)
    val username = args(6)
    val password = args(7)

    val dayRange = args(8).toInt
    val percentageFluctuation = args(9).toFloat / 100
    val updateStrategy = UpdateStrategy.from(args(10))

    val insertStatement =
      """
      INSERT INTO stock_prices (
        window_start,
        stock_id,
        close,
        low,
        high,
        volume
      ) VALUES (?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE close=?, low=?, high=?, volume=?
      """

    val numberOfRetries = 5
    val millisecondsBetweenAttempts = 5


    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.getConfig.setRestartStrategy(fixedDelayRestart(numberOfRetries, millisecondsBetweenAttempts))
    environment.registerCachedFile(meta, "meta-file")

//    val source = KafkaSource.builder[String]
//      .setBootstrapServers(server)
//      .setTopics(contentTopic)
//      .setGroupId(groupId)
//      .setStartingOffsets(OffsetsInitializer.earliest)
//      .setValueOnlyDeserializer(new SimpleStringSchema)
//      .build

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", server)
    properties.setProperty("group.id", groupId)
    val inputStream = environment.addSource(
      new FlinkKafkaConsumer[String](
        anomalyTopic,
        new SimpleStringSchema(),
        properties
      )
    )

    inputStream.map(_.toString).sinkTo(KafkaSinkFactory.create(server, anomalyTopic))

//    val stringStream = environment fromSource
//      (inputStream, WatermarkStrategy.noWatermarks(), s"Kafka ${contentTopic} Source")

//    val recordStream = stringStream
//      .map(_ split ",")
//      .map(intoStockPrice)
//      .assignTimestampsAndWatermarks(StockPriceWatermarkStrategy.create())
//      .map(_.toString)
//      .sinkTo(KafkaSinkFactory.create(server, anomalyTopic))

//    recordStream
//      .keyBy(_.stockId)
//      .window(TumblingEventTimeWindows of (Time days 30))
//      .aggregate(new StockPriceRecordAggregator, new StockPriceRecordProcessFunction)
//      .addSink(DatabaseSinkFactory.create[StockPriceRecordProcessFunction.Result](
//        insertStatement,
//        // Has to be verbose to ensure serialization for Spark preprocessor
//        new JdbcStatementBuilder[StockPriceRecordProcessFunction.Result] {
//          override def accept(statement: PreparedStatement, price: StockPriceRecordProcessFunction.Result): Unit = {
//            statement.setLong(1, price.start)
//            statement.setString(2, price.stockId)
//            statement.setFloat(3, price.close)
//            statement.setFloat(4, price.low)
//            statement.setFloat(5, price.high)
//            statement.setFloat(6, price.volume)
//            statement.setFloat(7, price.close)
//            statement.setFloat(8, price.low)
//            statement.setFloat(9, price.high)
//            statement.setFloat(10, price.volume)
//          }
//        },
//        url,
//        username,
//        password
//      ))

//    recordStream
//      .keyBy(_.stockId)
//      .window(TumblingEventTimeWindows of (Time days dayRange))
//      .aggregate(new StockPriceAnomalyAggregator, new StockPriceAnomalyProcessFunction)
//      .filter(_.fluctuation > percentageFluctuation)
//      .map(_.toString)
//      .sinkTo(KafkaSinkFactory.create(server, anomalyTopic))

    environment.execute("Stock prices processing...")
  }
}

