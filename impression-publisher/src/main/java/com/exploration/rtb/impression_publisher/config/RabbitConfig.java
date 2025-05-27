package com.exploration.rtb.impression_publisher.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange impressionExchange(
            @Value("${rtb.exchange.impressions}") String exchangeName
    ) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue impressionQueue(@Value("${rtb.queue.impressions}") String name) {
        return new Queue(name, true);
    }

    @Bean
    public Binding impressionBinding(
            Queue impressionQueue,
            DirectExchange impressionExchange,
            @Value("${rtb.routing.impressions}") String routingKey
    ) {
        return BindingBuilder.bind(impressionQueue).to(impressionExchange).with(routingKey);
    }
}
