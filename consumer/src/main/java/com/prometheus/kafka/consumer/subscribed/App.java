package com.prometheus.kafka.consumer.subscribed;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.prometheus.kafka.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Hello world!
 */
public final class App {

    private static final Logger LOG = Logger.getLogger("Subscribed-consumer");

    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.prometheus.kafka.deserializer.UserDeserializer");
        props.put("group.id", "test-21");
        // https://kafka.apache.org/documentation.html#consumerconfigs

        KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);

        // Here we are subscribing to the topic
        consumer.subscribe(Arrays.asList("users"));
        try {
            while (true) {
                // Poll loop is started
                ConsumerRecords<String, User> consumerRecords = consumer.poll(Duration.ofMillis(10));
                consumerRecords.forEach(record -> {
                    System.out.println("Topic: " + record.topic() + ", Offset: " + record.offset() + ", Partition: " + record.partition() + ", Key: " + record.key() + ", Value: " + record.value());
                });
            }
        } finally {
            consumer.close();
        }
    }

}
