import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;

public class IngestData {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-ingestdata");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Keep the data in the topic for the 1 day
        props.put("retention.ms", "86400000");

        Producer<String, String> producer = new KafkaProducer<>(props);
        String projectRootPath = System.getProperty("user.dir");
        String filePath = projectRootPath + "/data/data_small.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ProducerRecord<String, String> record = new ProducerRecord<>("example-data-small-topic", line);
                System.out.println(record.value());
                producer.send(record);
            }
        } finally {
            producer.close();
        }
    }
}
