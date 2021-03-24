package com.prometheus.kafka.consumer.streamconsumer;

import com.prometheus.kafka.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class StreamTopicConsumer1 {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093,localhost:9094");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.prometheus.kafka.deserializer.UserDeserializer");
        props.put("group.id", "stream-users-1");
        // https://kafka.apache.org/documentation.html#consumerconfigs

        KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);

        // Here we are subscribing to the topic
        consumer.subscribe(Arrays.asList("valid-users"));
        try {
            while (true) {
                // Poll loop is started
                ConsumerRecords<String, User> consumerRecords = consumer.poll(Duration.ofMillis(10));
                consumerRecords.forEach(record -> {
                    System.out.println("Topic: " + record.topic() + ", Offset: " + record.offset() + ", Partition: " + record.partition() + ", Key: " + record.key() + ", Value: [Valid-user]" + record.value());
                });
            }
        } finally {
            consumer.close();
        }
    }
}
