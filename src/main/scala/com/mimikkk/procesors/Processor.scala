package com.mimikkk.procesors

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.restartstrategy.RestartStrategies._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.connector.jdbc.JdbcStatementBuilder
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.{SlidingEventTimeWindows, TumblingEventTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.sql.PreparedStatement
import java.util.Properties
import scala.collection.JavaConverters.mapAsJavaMapConverter

object Processor {
  def main(args: Array[String]): Unit = {
    if (args.length != 11) {
      println("USAGE: Processor todo")
      System.exit(1)
    }

    final object configuration {
      val meta: String = args(0)

      final object kafka {
        val server: String = args(1)
        val groupId: String = args(2)
        val contentTopic: String = args(3)
        val anomalyTopic: String = args(4)
      }

      final object database {
        val url: String = args(5)
        val host: String = args(6)
        val port: String = args(7)
      }

      final object anomaly {
        val dayRange: Int = args(8).toInt
        val percentageFluctuation: Float = args(9).toFloat / 100
      }

      val updateStrategy = UpdateStrategy.from(args(10))
    }

    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.getConfig.setRestartStrategy {
      val numberOfRetries = 5
      val millisecondsBetweenAttempts = 500
      fixedDelayRestart(numberOfRetries, millisecondsBetweenAttempts)
    }
    environment.registerCachedFile(configuration.meta, "meta-file")

    val properties = new Properties {
      putAll(Map(
        "bootstrap.servers" -> args(1),
        "group.id" -> args(3)
      ).asJava)
    }

    val inputStream = environment.addSource(new FlinkKafkaConsumer[String](args(2), new SimpleStringSchema(), properties))
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

    environment.execute("Stock prices processing...")
  }
}