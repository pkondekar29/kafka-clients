package com.prometheus.kafka;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Hello world!
 */
public final class App {

    private static final Logger LOG = Logger.getLogger("Producer");

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 0; i < 1000; i++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("bigTopic", "abcdefghijklmnopqrstuvwxyz");
                LOG.info("Producing record: Message: " + i);
                producer.send(producerRecord);
            }
        } catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
        } finally {
            producer.close();
        }
    }
}
