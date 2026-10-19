package com.auction.core_service.config;

import com.auction.core_service.event.BidKafkaEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String BID_EVENTS_TOPIC = "auction-bid-events";

    @Bean
    public NewTopic bidEventsTopic() {
        return TopicBuilder.name(BID_EVENTS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public ProducerFactory<String, BidKafkaEvent> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        JsonSerializer<BidKafkaEvent> jsonSerializer = new JsonSerializer<>(objectMapper);
        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), jsonSerializer);
    }

    @Bean
    public KafkaTemplate<String, BidKafkaEvent> kafkaTemplate(ProducerFactory<String, BidKafkaEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }


    @Bean
    public ConsumerFactory<String, BidKafkaEvent> consumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "auction-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<BidKafkaEvent> jsonDeserializer = new JsonDeserializer<>(BidKafkaEvent.class, objectMapper);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BidKafkaEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, BidKafkaEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, BidKafkaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}