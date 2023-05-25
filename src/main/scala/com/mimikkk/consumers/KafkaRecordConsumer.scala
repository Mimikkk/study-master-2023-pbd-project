package com.mimikkk.consumers

import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util.Collections.singletonList
import java.util.Properties
import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsJavaMapConverter}

object KafkaRecordConsumer extends RecordConsumer {
  if (args.length != 3) {
    println("Usage: KafkaRecordConsumer <bootstrap-servers: kafka-server> <group-id: string> <topic: kafka-topic>")
    System.exit(1)
  }

  private final object configuration {
    val server: String = args(0)
    val id: String = args(1)
    val topic: String = args(2)
  }

  private final val properties = new Properties {
    putAll(Map(
      "bootstrap.servers" -> configuration.server,
      "group.id" -> configuration.id,
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    ).asJava)
  }

  private final val consumer = new KafkaConsumer[String, String](properties)
  consumer.subscribe(singletonList(configuration.topic))

  while (true) {
    consumer.poll(Duration ofSeconds 1000).asScala foreach (content => println(content value()))
  }

  consumer close()
}