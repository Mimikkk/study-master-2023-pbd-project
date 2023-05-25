source ./setup-variables.sh

java -cp "$(pwd)"/target/scala-2.12/*.jar com.mimikkk.consumers.KafkaRecordConsumer \
  "$KAFKA_BOOTSTRAP_SERVERS" \
  "$KAFKA_GROUP_ID" \
  "$KAFKA_ANOMALY_TOPIC_NAME"
