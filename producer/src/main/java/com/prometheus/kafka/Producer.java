package com.prometheus.kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import com.prometheus.model.User;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public final class Producer {

    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    private static final String PROPS_FILE_NAME = "producer-1.properties";

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        InputStream propsInputStream = new Producer().getClass().getClassLoader().getResourceAsStream(PROPS_FILE_NAME);
        try {
            props.load(propsInputStream);
        } catch (IOException e) {
            LOG.error("Could not read consumer properties", e);
            throw e;
        }
        KafkaProducer<String, User> producer = new KafkaProducer<>(props);

        try {
            for (int i = 0; i < 10; i++) {
                User user = User.builder().userId("Id-" + i).name("Name-" + i).build();
                String uuid = UUID.randomUUID().toString();
                ProducerRecord<String, User> producerRecord = new ProducerRecord<>("user", uuid, user);
                producer.send(producerRecord);
                LOG.info(String.format("Produced record: %s, User: %s", uuid, user.toString()));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            producer.close();
        }
    }
}
