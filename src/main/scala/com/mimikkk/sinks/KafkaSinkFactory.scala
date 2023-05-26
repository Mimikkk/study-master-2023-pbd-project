package com.mimikkk.sinks

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}

object KafkaSinkFactory {
  def create(server: String, topic: String): KafkaSink[String] =
    KafkaSink.builder()
      .setBootstrapServers(server)
      .setRecordSerializer(
        KafkaRecordSerializationSchema.builder()
          .setTopic(topic)
          .setValueSerializationSchema(new SimpleStringSchema())
          .build()
      )
      .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
      .build()
}
