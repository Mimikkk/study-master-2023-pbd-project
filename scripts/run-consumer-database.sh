source ./scripts/setup-variables.sh

java -cp "$(pwd)"/target/scala-2.12/*.jar com.mimikkk.consumers.DatabaseRecordConsumer \
  "$JDBC_URL" \
  "$JDBC_USERNAME" \
  "$JDBC_PASSWORD"
