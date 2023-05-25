source ./setup-variables.sh

echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add

sudo apt-get update
sudo apt-get install sbt
gsutil cp gs://"${BUCKET_NAME}"/resources/datasets/stock-prices/parts.zip "$INPUT_DIRECTORY_PATH".zip
gsutil cp gs://"${BUCKET_NAME}"/resources/datasets/stock-prices/meta.csv "$INPUT_FILE_PATH"
unzip -j "$INPUT_DIRECTORY_PATH".zip -d "$INPUT_DIRECTORY_PATH"
rm "$INPUT_DIRECTORY_PATH".zip

wget https://dlcdn.apache.org/flink/flink-1.14.4/flink-1.14.4-bin-scala_2.11.tgz -P "$HOME"
tar -xzf "$HOME/flink-1.14.4-bin-scala_2.11.tgz"
sbt clean assembly

kafka-topics.sh \
  --zookeeper "${CLUSTER_NAME}"-m:2181 \
  --create --replication-factor 1 \
  --partitions 1 \
  --topic "$KAFKA_ANOMALY_TOPIC_NAME"

kafka-topics.sh \
  --zookeeper "${CLUSTER_NAME}"-m:2181 \
  --create \
  --replication-factor 1 \
  --partitions 1 \
  --topic "$KAFKA_CONTENT_TOPIC_NAME"

mysql -u root -p < setup-database.sql
