package com.exploration.rtb.impression_publisher.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public FanoutExchange impressionExchange(@Value("${rtb.exchange.impressions}") String name) {
        return new FanoutExchange(name, true, false);
    }

    @Bean
    public Queue impressionsQueue(@Value("${rtb.queue.impressions}") String queueName) {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public Binding impressionsBinding(Queue impressionsQueue, FanoutExchange impressionExchange) {
        return BindingBuilder.bind(impressionsQueue).to(impressionExchange);
    }

    @Bean
    public DirectExchange bidsExchange(
            @Value("${rtb.exchange.bids}") String bidsExchange) {
        return new DirectExchange(bidsExchange, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
