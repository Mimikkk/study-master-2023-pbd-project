source ./scripts/setup-variables.sh

java -cp "$(pwd)"/target/scala-2.12/*.jar com.mimikkk.procesors.KafkaProcessor \
 "$INPUT_FILE_PATH" \
 "$KAFKA_BOOTSTRAP_SERVERS" \
 "$KAFKA_GROUP_ID" \
 "$KAFKA_CONTENT_TOPIC_NAME" \
 "$KAFKA_ANOMALY_TOPIC_NAME" \
 "$JDBC_URL" \
 "$JDBC_USERNAME" \
 "$JDBC_PASSWORD" \
 "$ANOMALY_STOCK_DAYS_RANGE" \
 "$ANOMALY_STOCK_PERCENT_FLUCTUATION" \
 "$PROCESSING_TYPE"
