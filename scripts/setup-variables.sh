# Cloud parameters
export CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
export BUCKET_NAME="pbd-23-dz"
export HADOOP_CLASSPATH=`hadoop classpath`
export HADOOP_CONF_DIR=/etc/hadoop/conf
export INPUT_FILE_PATH="$HOME/resources/datasets/stock-prices/meta.csv"
export INPUT_DIRECTORY_PATH="$HOME/resources/datasets/stock-prices/parts"

# Kafka parameters
export KAFKA_PRODUCER_SLEEP_TIME=1
export KAFKA_CONTENT_TOPIC_NAME="stock-prices-records"
export KAFKA_ANOMALY_TOPIC_NAME="stock-prices-anomalies"
export KAFKA_BOOTSTRAP_SERVERS="${CLUSTER_NAME}-w-0:9092"
export KAFKA_GROUP_ID="stock-prices-group"

# Database parameters
export JDBC_URL="jdbc:mysql://${CLUSTER_NAME}-m:3306/stock-prices"
export JDBC_USERNAME="stream-user"
export JDBC_PASSWORD="password"

# Flink parameters
export FLINK_DIRECTORY="$HOME/flink-1.15.4"

# Anomaly parameters
export ANOMALY_STOCK_DAYS_RANGE=7
export ANOMALY_STOCK_PERCENT_FLUCTUATION=40

export PROCESSING_TYPE="historical"
