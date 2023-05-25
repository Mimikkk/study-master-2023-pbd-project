# Project 2 -- 2023 -- Big Data -- Flink DataStream

## Description

Processing stream data using Flink DataStream API with Kafka as a message broker.

Dataset used for this exercise is a modified Stock Data Dataset
from [Kaggle](https://www.kaggle.com/jacksoncrow/stock-market-dataset).

## Example cloud environment:

- [Google Cloud Platform](https://cloud.google.com/)
- [Google Cloud Storage](https://cloud.google.com/storage)
- [Google Cloud Dataproc](https://cloud.google.com/dataproc)
- [setup-cluster.sh](./scripts/setup-cluster.sh) - Script file to set up the cluster on [Google Cloud Platform](https://cloud.google.com/) using
  [Google Cloud Dataproc](https://cloud.google.com/dataproc).

```shell
gcloud dataproc clusters create "${CLUSTER_NAME}" \
  --enable-component-gateway --bucket "${BUCKET_NAME}" \
  --region "${REGION}" --subnet default --zone "${ZONE}" \
  --master-machine-type n1-standard-4 --master-boot-disk-size 50 \
  --num-workers 2 \
  --worker-machine-type n1-standard-2 --worker-boot-disk-size 50 \
  --image-version 2.0-debian10 \
  --project "${PROJECT_ID}" --max-age=3h \
  --optional-components=ZEPPELIN,DOCKER,ZOOKEEPER \
  --metadata "run-on-master=true" \
  --initialization-actions \
  gs://goog-dataproc-initialization-actions-"${REGION}"/kafka/kafka.sh
```

#### Variables

- `${CLUSTER_NAME}` is the name of the cluster
- `${BUCKET_NAME}` is the name of the bucket
- `${REGION}` is the region where the cluster will be created
- `${ZONE}` is the zone where the cluster will be created
- `${PROJECT_ID}` is the project id

#### Flags

- `--enable-component-gateway` is the component gateway to access the cluster
- `--bucket ${BUCKET_NAME}` is the bucket to store the data
- `--region ${REGION}` is the region where the cluster will be created
- `--subnet default` is the subnet of the cluster
- `--zone ${ZONE}` is the zone where the cluster will be created
- `--master-machine-type n1-standard-4` is the machine type of the master node
- `--num-workers 2` is the number of workers
- `--master-boot-disk-size 50` is the boot disk size of the master node
- `--worker-machine-type n1-standard-2` is the machine type of the worker node
- `--worker-boot-disk-size 50` is the boot disk size of the worker node
- `--image-version 2.0-debian10` is the image version of the cluster
- `--project ${PROJECT_ID}` is the project id
- `--max-age=3h` is the maximum age of the cluster
- `--optional-components=ZEPPELIN,DOCKER,ZOOKEEPER` is the optional components to install on the cluster
    - `ZEPPELIN` is the Zeppelin notebook which is used to run the Flink DataStream application
    - `DOCKER` is the Docker to run the Kafka
    - `ZOOKEEPER` is the Zookeeper to run the Kafka
- `--metadata "run-on-master=true"` is the metadata to run Kafka on the master node
- `--initialization-actions` is the initialization script for Kafka

#### Kafka broker

```shell
gs://goog-dataproc-initialization-actions-${REGION}/kafka/kafka.sh
```

## Scripts

All scripts are located in the [scripts](./scripts) directory.

Setup scripts:

- [setup-variables.sh](./scripts/setup-variables.sh) is used to set up the variables.
- [setup-environment.sh](./scripts/setup-environment.sh) is used to set up the environment.
- [setup-cluster.sh](./scripts/setup-cluster.sh) is used to set up the cluster.
- [setup-database.sql](./scripts/setup-database.sql) is used to set up the database.

Run scripts:

- [run-consumer-database.sh](./scripts/run-consumer-database.sh) is used to run the consumer to store the data in the
  database.
- [run-consumer-kafka.sh](./scripts/run-consumer-kafka.sh) is used to run the consumer to print the data.
- [run-producer.sh](./scripts/run-producer-kafka.sh) is used to run the producer to send the data.
- [run-processor.sh](./scripts/run-processor.sh) is used to run the processor to process the data.
