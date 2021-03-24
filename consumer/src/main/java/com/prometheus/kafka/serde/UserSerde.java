package com.prometheus.kafka.serde;

import com.prometheus.kafka.deserializer.UserDeserializer;
import com.prometheus.kafka.model.User;
import com.prometheus.kafka.serializer.UserSerializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class UserSerde implements Serde<User> {

    private Serializer<User> serializer;
    private Deserializer<User> deserializer;

    public UserSerde() {
        this.serializer = new UserSerializer();
        this.deserializer = new UserDeserializer();
    }

    @Override
    public Serializer<User> serializer() {
        return this.serializer;
    }

    @Override
    public Deserializer<User> deserializer() {
        return this.deserializer;
    }
    
}