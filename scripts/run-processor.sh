source ./scripts/setup-variables.sh

"$FLINK_DIRECTORY"/bin/flink run \
  -m yarn-cluster -p 2 -yjm 1024m -ytm 2048m \
  -c com.mimikkk.procesors.KafkaProcessor "$(pwd)"/target/scala-2.12/*.jar \
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
  "historical"
