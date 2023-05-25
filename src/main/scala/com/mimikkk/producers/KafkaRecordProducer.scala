package com.mimikkk.producers

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.io.File
import java.nio.file.Files._
import java.nio.file.Paths
import java.util.Properties
import java.util.concurrent.TimeUnit.SECONDS
import java.util.function.Consumer
import scala.collection.JavaConverters._

class KafkaRecordProducer extends RecordProducer {
  if (args.length != 4) {
    println("Usage: KafkaRecordProducer <directory: path> <secondsBetweenStreams: int> <topic: kafka-topic> <bootstrap.servers: kafka-server>")
    System.exit(1)
  }

  private final val directory = args(0)
  private final val secondsPerStreams = args(1).toInt
  private final val topic = args(2)
  private final val properties = new Properties {
    putAll(
      Map(
        "bootstrap.servers" -> args(3),
        "acks" -> "all",
        "retries" -> "0",
        "batch.size" -> "16384",
        "linger.ms" -> "1",
        "buffer.memory" -> "33554432",
        "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer"
      ).asJava
    )
  }

  private final val producer = new KafkaProducer[String, String](properties)
  private final val paths = new File(directory) listFiles() map (_.getAbsolutePath)

  paths.sorted foreach (path => try {
    lines(Paths get path) skip 1 forEach new Consumer[String] {
      override def accept(row: String): Unit =
        producer send new ProducerRecord(topic, row.split(',')(0), row)
    }
    SECONDS sleep secondsPerStreams
  } catch {
    case e: Throwable => e printStackTrace()
  })

  producer close()
}
