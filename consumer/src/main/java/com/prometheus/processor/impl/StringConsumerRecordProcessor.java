package com.prometheus.processor.impl;

import com.prometheus.processor.api.ConsumerRecordProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class StringConsumerRecordProcessor implements ConsumerRecordProcessor<String, String> {

    @Override
    public void accept(ConsumerRecord<String, String> record) {
        System.out.println(String.format("Topic: %s, Partition: %s, Offset: %s, Value: %s", record.topic(),
                record.partition(), record.offset(), record.value().toUpperCase()));
    }

}