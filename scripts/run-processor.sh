source ./setup-variables.sh

"$FLINK_DIRECTORY"/bin/flink run \
  -m yarn-cluster -p 2 -yjm 1024m -ytm 2048m \
  -c com.mimikkk.processors.Processor "$(pwd)"/target/scala-2.11/*.jar \
  "$INPUT_FILE_PATH" \
  "$KAFKA_BOOTSTRAP_SERVERS" \
  "$KAFKA_DATA_TOPIC_NAME" \
  "$KAFKA_GROUP_ID" \
  "$JDBC_URL" \
  "$JDBC_USERNAME" \
  "$JDBC_PASSWORD" \
  "$ANOMALY_PERIOD_LENGTH" \
  "$ANOMALY_RATING_COUNT" \
  "$ANOMALY_RATING_MEAN" \
  "$KAFKA_ANOMALY_TOPIC_NAME"
