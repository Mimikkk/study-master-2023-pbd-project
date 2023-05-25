source ./scripts/setup-variables.sh

java -cp "$(pwd)"/target/scala-2.12/*.jar com.mimikkk.producers.KafkaRecordProducer \
  "$INPUT_DIRECTORY_PATH" \
  "$KAFKA_PRODUCER_SLEEP_TIME" \
  "$KAFKA_CONTENT_TOPIC_NAME" \
  "$KAFKA_BOOTSTRAP_SERVERS"
