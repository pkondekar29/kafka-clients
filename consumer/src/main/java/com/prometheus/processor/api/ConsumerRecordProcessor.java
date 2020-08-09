package com.prometheus.processor.api;

import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerRecordProcessor<K, V> extends Consumer<ConsumerRecord<K, V>> {

}