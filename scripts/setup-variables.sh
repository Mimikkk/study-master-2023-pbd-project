# Cloud parameters
export BUCKET_NAME="pbd-23-dz"
export CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
export HADOOP_CONF_DIR=/etc/hadoop/conf
export HADOOP_CLASSPATH=$(hadoop classpath)

export INPUT_DIRECTORY_PATH="$HOME/resources/datasets/stock-prices/parts"
export INPUT_FILE_PATH="$HOME/resources/datasets/stocks-prices/meta.csv"

# Kafka parameters
export KAFKA_PRODUCER_SLEEP_TIME=30
export KAFKA_CONTENT_TOPIC_NAME="stock-prices"
export KAFKA_ANOMALY_TOPIC_NAME="stock-prices-anomalies"
export KAFKA_BOOTSTRAP_SERVERS="${CLUSTER_NAME}-w-0:9092"
export KAFKA_GROUP_ID="stock-prices-group"

# Database parameters
export JDBC_URL="jdbc:mysql://${CLUSTER_NAME}-m:3306/stock-prices"
export JDBC_USERNAME="stream-user"
export JDBC_PASSWORD="password"

# Flink parameters
export FLINK_DIRECTORY="$HOME/flink-1.14.4"

# Anomaly parameters
export ANOMALY_STOCK_DAYS_RANGE=7
export ANOMALY_STOCK_PERCENT_FLUCTUATION=4

export PROCESSING_TYPE="historical"
