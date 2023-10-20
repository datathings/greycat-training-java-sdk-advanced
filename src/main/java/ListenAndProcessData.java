import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import ai.greycat.GreyCat;
import ai.greycat.std;

import java.time.Duration;
import java.util.Arrays;

public class ListenAndProcessData {
    public static void main(String[] args) throws Exception {
        GreyCat greycat = new GreyCat("http://localhost:8080");
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "streams-topic-input-file-consumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("example-data-small-topic"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                std.core.Array<?>  data = (std.core.Array<?> ) GreyCat.call(greycat, "project::accumulateData", record.value());
                System.out.println("API Result: " + data);
            }
        }
    }
}
