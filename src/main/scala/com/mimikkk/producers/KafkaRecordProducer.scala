package com.mimikkk.producers

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.io.File
import java.nio.file.Files._
import java.nio.file.Paths
import java.util.Properties
import java.util.concurrent.TimeUnit.SECONDS
import java.util.function.Consumer
import scala.collection.JavaConverters._

object KafkaRecordProducer extends RecordProducer {
  if (args.length != 4) {
    println("Usage: KafkaRecordProducer <parts-directory: path> <seconds-between-parts: int> <topic: kafka-topic> <bootstrap.servers: kafka-server>")
    System.exit(1)
  }

  private final object configuration {
    final val partsDirectory = args(0)
    final val secondsBetweenParts = args(1).toInt
    final val topic = args(2)
    final val server = args(3)
  }

  private final val properties = new Properties {
    putAll(Map(
      "bootstrap.servers" -> configuration.server,
      "acks" -> "all",
      "retries" -> "0",
      "batch.size" -> "16384",
      "linger.ms" -> "1",
      "buffer.memory" -> "33554432",
      "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
      "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer"
    ).asJava)
  }

  private final val producer = new KafkaProducer[String, String](properties)
  private final val partsPaths = new File(configuration.partsDirectory) listFiles() map (_.getAbsolutePath)

  partsPaths.sorted foreach (path => try {
    println("< Stream Sent >")
    lines(Paths get path) skip 1 forEach (
      row => producer send new ProducerRecord(configuration.topic, row.split(',')(0), row)
    )
    SECONDS sleep configuration.secondsBetweenParts
  } catch {
    case e: Throwable => e printStackTrace()
  })

  producer close()
}
