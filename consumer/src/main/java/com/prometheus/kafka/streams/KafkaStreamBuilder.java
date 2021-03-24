package com.prometheus.kafka.streams;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.prometheus.kafka.deserializer.UserDeserializer;
import com.prometheus.kafka.model.User;
import com.prometheus.kafka.serde.UserSerde;
import com.prometheus.kafka.serializer.UserSerializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class KafkaStreamBuilder {

    public static void main(String[] args) {
        Properties props = new Properties();

        // This also becomes the group id for consumers
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-segregator");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093,localhost:9094");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

//        Map<String, Object> serdeProps = new HashMap<>();
//        final Serializer<User> serializer = new UserSerializer();
//        serdeProps.put("UserPOJO", User.class);
//        serializer.configure(serdeProps, false);
//        final Deserializer<User> deserializer = new UserDeserializer();
//        serdeProps.put("UserPOJO", User.class);
//        deserializer.configure(serdeProps, false);

        Serde<User> userSerde = new UserSerde();

        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, userSerde.getClass().getName());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, User> stream = streamsBuilder.stream("users", Consumed.with(Serdes.String(), userSerde));

        KStream<String, User>[] lengthBranched =
            stream
                .peek(KafkaStreamBuilder::print)
                .filter((key, user) -> Objects.nonNull(user))
                .branch((key, user) -> {
                        String c = user.getUserId() != null ? user.getUserId().substring(0, 1) : "1";
                        return Integer.parseInt(c) > 5;
                    },
                    (key, user) -> {
                        String c = user.getUserId() != null ? user.getUserId().substring(0, 1) : "1";
                        return Integer.parseInt(c) < 5;
                    });

        lengthBranched[0]
            .mapValues(user -> {
                user.setName(user.getName().toUpperCase());
                return user;
            })
            .peek(KafkaStreamBuilder::print)
            .to("valid-users", Produced.with(Serdes.String(), userSerde));

        lengthBranched[1]
            .peek(KafkaStreamBuilder::print)
            .to("invalid-users", Produced.with(Serdes.String(), userSerde));

        Topology topology = streamsBuilder.build();

        KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public static void print(String key, User user) {
        if(user != null) {
            System.out.println("Key: [" + key + "], user: [" + user.toString() + "]");
        }
    }

    public static void print(User user) {
        if(user != null)
            System.out.println("user: [" + user.toString() + "]");
    }

}
