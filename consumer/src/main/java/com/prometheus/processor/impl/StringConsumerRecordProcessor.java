package com.prometheus.processor.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class StringConsumerRecordProcessor extends AbstractConsumerRecordProcessor<String, String> {

    @Override
    public void accept(ConsumerRecord<String, String> record) {
        System.out.println(String.format("Topic: %s, Partition: %s, Offset: %s, Value: %s", record.topic(),
                record.partition(), record.offset(), record.value().toUpperCase()));
    }

}