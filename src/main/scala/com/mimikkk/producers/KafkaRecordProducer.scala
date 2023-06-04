package com.mimikkk.producers

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.io.File
import java.nio.file.Files._
import java.nio.file.Paths
import java.util.Properties
import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._

object KafkaRecordProducer extends RecordProducer {
  if (args.length != 4) {
    println(
      """
        |Usage of KafkaRecordProducer:
        |  <dataset-parts-directory: path-string>
        |  <seconds-between-parts: int>
        |  <topic: string>
        |  <bootstrap-server: server-string>
      """.stripMargin
    )
    System.exit(1)
  }

  final val partsDirectory = args(0)
  final val secondsBetweenParts = args(1).toInt
  final val topic = args(2)
  final val server = args(3)

  private final val properties = new Properties {
    putAll(Map(
      "bootstrap.servers" -> server,
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
  private final val partsPaths = new File(partsDirectory) listFiles() map (_.getAbsolutePath)

  println("Start broadcasting...")
  partsPaths.sorted.zipWithIndex foreach (pair => try {
    val (path, index) = pair

    println(s"< Stream '${index + 1}/${partsPaths.length}' Sending... >")
    lines(Paths get path) skip 1 forEach (
      row => {
        println("  " + row)
        producer send new ProducerRecord[String, String](topic, row.split(',')(0), row)
      }
      )
    println(s"< Stream Sent. >")
    TimeUnit.SECONDS.sleep(secondsBetweenParts)
  } catch {
    case e: Throwable => e printStackTrace()
  })
  println("Finished broadcasting.")

  producer close()
}
