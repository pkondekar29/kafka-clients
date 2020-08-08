package com.prometheus.kafka.consumer.group;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.prometheus.kafka.consumer.utils.Recorder;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;

public class Consumer1 {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093, localhost:9094, localhost:9095");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "test-group");
        // https://kafka.apache.org/documentation.html#consumerconfigs

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Here we are subscibing to the topic
        consumer.subscribe(Arrays.asList("bigTopic"));
        try {
            Recorder recorder = Recorder.getInstance();
            AtomicInteger i = new AtomicInteger(0);
            while (true) {
                // Poll loop is started
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(10));
                consumerRecords.forEach(record -> {
                    System.out.println(String.format("Count: %s, Topic: %s, Partition: %s, Offset: %s, Value: %s",
                            Integer.toString(i.incrementAndGet()), record.topic(), record.partition(), record.offset(),
                            record.value().toUpperCase()));
                    if(recorder.containsKey(record.offset())) {
                        recorder.put(record.offset(), recorder.get(record.offset()) + 1);
                    } else {
                        recorder.put(record.offset(), 1);
                    }
                });
                consumer.commitAsync((offsets, exception) -> {
                    String dupString = 
                        recorder.entrySet().stream()
                            .filter(entry -> {
                                return entry.getValue().intValue() > 1;
                            })
                            .map(Entry::getKey)
                            .map(offset -> Long.toString(offset))
                            .collect(Collectors.joining(","));
                    if(dupString.length() > 1) {
                        System.out.println(String.format("Number of offsets: %s, Commited Offsets: %s, Duplicated messages: [%s]",
                            Integer.toString(offsets.size()),
                            offsets.entrySet().stream()
                                .map(Entry::getValue)
                                .map(OffsetAndMetadata::offset)
                                .map(offset -> Long.toString(offset))
                                .collect(Collectors.joining(", ")),
                            dupString));
                    }
                });
            }
        } finally {
            consumer.close();
        }
    }
}