package com.prometheus.processor.impl;

import java.util.concurrent.Semaphore;

import com.prometheus.processor.api.ConsumerRecordProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public abstract class AbstractConsumerRecordProcessor<K, V> implements ConsumerRecordProcessor<K, V> {

	public void handle(ConsumerRecord<K, V> record, Throwable exception) {

    }
    
    // private Semaphore semaphore = null;

    // public AbstractConsumerRecordProcessor(Semaphore semaphore) {
    //     this.semaphore = semaphore;
    // }

    // public abstract void process(ConsumerRecord<K, V> record);

}