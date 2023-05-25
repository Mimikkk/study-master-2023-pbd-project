# Cloud parameters
export BUCKET_NAME="flink-pbd-2023"
export CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
export HADOOP_CONF_DIR=/etc/hadoop/conf
export HADOOP_CLASSPATH=$(hadoop classpath)
export INPUT_DIRECTORY_PATH="$HOME/resources/datasets/stocks/streams"
export INPUT_FILE_PATH="$HOME/resources/datasets/stocks/meta.csv"

# Kafka parameters
export KAFKA_PRODUCER_SLEEP_TIME=30
export KAFKA_DATA_TOPIC_NAME="stock"
export KAFKA_ANOMALY_TOPIC_NAME="stock-anomalies"
export KAFKA_BOOTSTRAP_SERVERS="${CLUSTER_NAME}-w-0:9092"
export KAFKA_GROUP_ID="stock-group"

# Database parameters
export JDBC_URL="jdbc:mysql://${CLUSTER_NAME}-m:3306/stock"
export JDBC_USERNAME="stream-user"
export JDBC_PASSWORD="stream"

# Flink parameters
export FLINK_DIRECTORY="$HOME/stock-flink/flink-1.14.4"

# Processor parameters
export ANOMALY_PERIOD_LENGTH=30
export ANOMALY_RATING_COUNT=70
export ANOMALY_RATING_MEAN=4
export PROCESSING_TYPE="H"
