# Java SDK Advanced

This is a training for Java SDK of GreyCat. Here we use an example how you can integrate Kafka with GreyCat in real-time.

## Prerequisites

### Java

- Java >= 8
- Maven 3
- Kafka (We are using 3.6 version in this tutorial)

### Dataset

In this training we use the following dataset:

> Patrick Fleith. (2023). Controlled Anomalies Time Series (CATS) Dataset (Version 2) [Data set]. Solenix Engineering GmbH. https://doi.org/10.5281/zenodo.8338435

```bash
mkdir -p data
curl -L https://huggingface.co/datasets/patrickfleith/controlled-anomalies-time-series-dataset/resolve/main/data.csv > data/dataset.csv
head -n10001 data/dataset.csv > data/data_small.csv
```

## Run

- Following [Apache Kakfa quickstart](https://kafka.apache.org/quickstart) instructions, start both the ZooKeeper & Kakfa broker services.

- Start the GreyCat server:
  ```bash
  greycat serve --user=1
  ```
  - By default the disk storage is limited to 10240MB. If one wants to import the whole dataset it might be necessary to raise this limit:
    ```bash
    echo 'GREYCAT_STORE=16384' > .env
    ```
    It is important to notice that on Windows systems, a space of this size is fully reserved as soon as GreyCat starts.

- Start the Kafka listener:
  ```bash
  mvn package exec:java -Dexec.mainClass=KafkaListener
  ```
  This will start listening to a Kafka topic called `example-data-small-topic` on the 9092 port.
  On each incoming event, it will call GreyCat `project::accumulateData` endpoint on port 8080 to process the record from the data_small.csv file.
  
- Run the Kafka publisher:
  ```bash
  mvn package exec:java -Dexec.mainClass=KafkaPublisher
  ```
  This will publish each line from `data_small.csv` file as a separate event into Kafka topic.
  
- Wait for a few seconds. You should notice that in real-time in the terminal for the listener the events are being listened and processed by GreyCat.
