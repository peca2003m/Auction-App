package com.auction.core_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String AUCTION_CLOSED_QUEUE = "auction.closed";
    public static final String BID_PLACED_QUEUE = "auction.bid.placed";
    public static final String AUCTION_EXCHANGE = "auction.exchange";

    @Bean
    public Queue auctionClosedQueue() {
        return new Queue(AUCTION_CLOSED_QUEUE, true);
    }

    @Bean
    public Queue bidPlacedQueue() {
        return new Queue(BID_PLACED_QUEUE, true);
    }

    @Bean
    public TopicExchange auctionExchange() {
        return new TopicExchange(AUCTION_EXCHANGE);
    }

    @Bean
    public Binding auctionClosedBinding() {
        return BindingBuilder
                .bind(auctionClosedQueue())
                .to(auctionExchange())
                .with("auction.closed");
    }

    @Bean
    public Binding bidPlacedBinding() {
        return BindingBuilder
                .bind(bidPlacedQueue())
                .to(auctionExchange())
                .with("auction.bid.placed");
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}