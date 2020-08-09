package com.prometheus.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.model.User;

import org.apache.kafka.common.serialization.Serializer;

public class UserSerializer implements Serializer<User> {

    @Override
    public byte[] serialize(String topic, User data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}