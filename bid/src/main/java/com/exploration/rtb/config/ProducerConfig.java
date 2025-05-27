package com.exploration.rtb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {

    @Bean
    public DirectExchange exchange(@Value("${rtb.exchange}") String exchange){
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue queue(@Value("${rtb.routing-key}") String routingKey){
        return new Queue(routingKey, true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange,
                           @Value("${rtb.routing-key}") String key) {
        return BindingBuilder.bind(queue).to(exchange).with(key);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
