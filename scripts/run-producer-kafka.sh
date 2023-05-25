source ./setup-variables.sh

java -cp "$(pwd)"/target/scala-2.11/*.jar com.mimikkk.producers.KafkaRecordProducer \
  "$INPUT_DIRECTORY_PATH" \
  "$KAFKA_PRODUCER_SLEEP_TIME" \
  "$KAFKA_DATA_TOPIC_NAME" \
  "$KAFKA_BOOTSTRAP_SERVERS"
