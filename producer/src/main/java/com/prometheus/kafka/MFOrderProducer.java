package com.prometheus.kafka;

import com.prometheus.enums.OrderStatus;
import com.prometheus.enums.Product;
import com.prometheus.enums.User;
import com.prometheus.model.MFOrderEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class MFOrderProducer {
    private static final Logger LOG = LoggerFactory.getLogger(MFOrderProducer.class);

    private static final String PROPS_FILE_NAME = "mf-producer.properties";

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
            LOG.error("Could not read producer properties", e);
            throw e;
        }
        KafkaProducer<String, MFOrderEvent> producer = new KafkaProducer<>(props);

        try {
            Random random = new Random();
            for (int i = 0; i < 2; i++) {
                MFOrderEvent user = MFOrderEvent.builder()
                    .orderId(UUID.randomUUID().toString())
                    .companyName("CloudTail Pvt Ltd")
                    .productName(Product.getRandom().toString())
                    .userName(User.getRandom().name())
                    .quantity(random.nextInt(1000))
                    .orderStatus(OrderStatus.getRandom().name())
                    .build();
                String messageKey = UUID.randomUUID().toString();
                ProducerRecord<String, MFOrderEvent> producerRecord = new ProducerRecord<>("mf-orders", messageKey, user);
                producer.send(producerRecord);
                LOG.info(String.format("Produced record: %s, User: %s", messageKey, user.toString()));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            producer.close();
        }
    }
}
