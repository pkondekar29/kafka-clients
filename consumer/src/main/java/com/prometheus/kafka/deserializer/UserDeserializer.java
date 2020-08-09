package com.prometheus.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.kafka.model.User;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDeserializer implements Deserializer<User> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDeserializer.class);

    @Override
    public User deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(data, User.class);
        } catch (Exception e) {
            LOG.error("Unexpected error", e);
        }
        return user;
    }

}