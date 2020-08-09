package com.prometheus.processor.impl;

import com.prometheus.kafka.model.User;
import com.prometheus.processor.api.ConsumerRecordProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class UserConsumerRecordProcessor implements ConsumerRecordProcessor<String, User> {

    @Override
    public void accept(ConsumerRecord<String, User> record) {
        System.out.println(String.format("Topic: %s, Partition: %s, Offset: %s, Key: %s, User: %s", record.topic(),
                record.partition(), record.offset(), record.key(), record.value().toString()));
    }

}