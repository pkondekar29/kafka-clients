package com.prometheus.kafka.consumer.group;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.prometheus.processor.impl.AbstractConsumerRecordProcessor;
import com.prometheus.processor.impl.UserConsumerRecordProcessor;
import com.prometheus.kafka.consumer.utils.Recorder;
import com.prometheus.kafka.model.User;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer1 {

    private static final Logger LOG = LoggerFactory.getLogger(Consumer1.class);

    private static final String PROPS_FILE_NAME = "consumer-1.properties";

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        InputStream propsInputStream = new Consumer1().getClass().getClassLoader().getResourceAsStream(PROPS_FILE_NAME);
        try {
            props.load(propsInputStream);
        } catch (IOException e) {
            LOG.error("Could not read consumer properties", e);
            throw e;
        }
        // https://kafka.apache.org/documentation.html#consumerconfigs

        KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);

        // Here we are subscibing to the topic
        consumer.subscribe(Collections.singletonList("users"));
        try {
            Recorder recorder = Recorder.getInstance();
            AtomicInteger i = new AtomicInteger(0);

            while (true) {
                // Poll loop is started
                ConsumerRecords<String, User> consumerRecords = consumer.poll(Duration.ofMillis(100));

                // Convert Collection from Iterable
                final List<ConsumerRecord<String, User>> consumerRecordCollection =
                    StreamSupport.stream(consumerRecords.spliterator(), true)
                        .collect(Collectors.toList());

                // To start the execution
                CompletableFuture<Void> startEvent = new CompletableFuture<>();

                startEvent.thenApplyAsync(nil -> {
                    List<CompletableFuture<Void>> taskFutures = consumerRecordCollection.stream()
                        .map(record -> {
                            AbstractConsumerRecordProcessor<String, User> processor = new UserConsumerRecordProcessor();
                            return CompletableFuture
                                // Process the record
                                .runAsync(() -> processor.accept(record))
                                // Check for duplicate processing
                                .whenComplete((offset, exception) -> {
                                    // Handle exception
                                    processor.handle(record, exception);

                                    // Keep a count for monitoring
                                    recorder.computeIfAbsent(record.offset(), o -> new LongAdder()).increment();
                                    if (recorder.get(record.offset()).longValue() > 1L) {
                                        LOG.info("Duplicate processing: " + record.offset());
                                    }
                                    // Increment the count of records processed by this consumer
                                    LOG.info("Count: " + i.incrementAndGet());
                                });
                        })
                        .collect(Collectors.toList());

                    return CompletableFuture.allOf((CompletableFuture<?>[]) taskFutures.toArray());
                }).thenRun(() -> {
                    consumer.commitAsync((offsets, exception) -> {
                        if (offsets.size() > 0) {
                            LOG.info(String.format("Commited Offsets: %s",
                                    offsets.entrySet().stream().map(Entry::getValue).map(OffsetAndMetadata::offset)
                                            .map(offset -> Long.toString(offset)).collect(Collectors.joining(", "))));
                        }
                    });
                });
            }
        } catch (Exception e) {
            LOG.error("Unexpected error", e);
        } finally {
            consumer.close();
        }
    }
}
