# Java SDK Advanced

This is a training for Java SDK of GreyCat. Here we use an example how you can integrate Kafka with GreyCat in real-time.

## Prerequisites

### Python

- Java >= 8
- Maven 3
- Kafka (We are using 3.6 version in this tutorial)

### Dataset

In this training we use the following dataset:

> Patrick Fleith. (2023). Controlled Anomalies Time Series (CATS) Dataset (Version 2) [Data set]. Solenix Engineering GmbH. https://doi.org/10.5281/zenodo.8338435

It's already located in the repository under the name: ```data_small.csv```

### GreyCat setup. It's already setup in this project.

- GreyCat runtime: https://get.greycat.io/
- Java:
  - Maven:
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project>
      […]
      <properties>
        […]
        // GreyCat
        <greycat.version.branch>dev</greycat.version.branch>
        <greycat.version.major>6.3</greycat.version.major>
        <greycat.version.minor>9</greycat.version.minor>
        <greycat.version>${greycat.version.major}.${greycat.version.minor}-${greycat.version.branch}</greycat.version>
        // Kafka
        <kafka.version>3.6.0</kafka.version>
      </properties>
      […]
      <dependencies>
        […]
        <dependency>
          <groupId>ai.greycat</groupId>
          <artifactId>sdk</artifactId>
            <version>${greycat.version}</version>
        </dependency>
        <dependency>
          <groupId>org.apache.kafka</groupId>
          <artifactId>kafka-streams</artifactId>
          <version>${kafka.version}</version>
        </dependency>
        […]
      </dependencies>
      […]
      <repositories>
        […]
        <repository>
          <name>GreyCat Java SDK repository</name>
          <id>get.greycat.io</id>
          <url>https://get.greycat.io/files/sdk/java/${greycat.version.branch}/${greycat.version.major}/</url>
          <layout>default</layout>
        </repository>
      </repositories>
      […]
    </project>
    ```
  - Gradle:
    ```json
    TODO
    ```
As the version above is doomed to be outdated, more recent versions can be checked at https://get.greycat.io/files/sdk/java/testing/

## Start your tutorial here. Start your servers.

- Go to you Kafka directory and start your Kafka ZooKeeper
  
  ```bin/zookeeper-server-start.sh config/zookeeper.properties```

- Open a new terminal and start the Kafka broker service
  
  ```bin/kafka-server-start.sh config/server.properties```

- Go to the root of the project, same folder where the project.gcl file is located and start the greycat server on another terminal
  
  ```greycat serve --user=1```

### Start listening to the Kafka stream

- In the root of the project run listener:
  
  ```mvn package exec:java -Dexec.mainClass=ListenAndProcessData```

  This will start listening to Kafka topic called ```example-data-small-topic``` on the 9092 port.
  On each upcoming event, it will call GreyCat ```project::accumulateData``` endpoint on port 8080 to process the record from the data_small.csv file.

- Open another terminal and injest data to the same Kafka topic.
  
  ```mvn package exec:java -Dexec.mainClass=IngestData```
  
  This will injest ach line in the data_small.csv file as a separate event to the Kafka topic.

- Wait for 5-10 seconds and you could notice that in real-time in the terminal for the listener the events are being listened and processed by Greycat.
