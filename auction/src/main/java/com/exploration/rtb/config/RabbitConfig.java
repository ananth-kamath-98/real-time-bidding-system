package com.exploration.rtb.config;

import com.exploration.rtb.dto.AdBid;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange directExchange(@Value("${rtb.exchange}") String exchange) {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue queue(@Value("${rtb.routing-key}") String routingKey) {
        return new Queue(routingKey, true);
    }

    @Bean
    public Binding binding(Queue q, DirectExchange ex,
                           @Value("${rtb.routing-key}") String key) {
        return BindingBuilder.bind(q).to(ex).with(key);
    }

    @Bean
    public RedisTemplate<String, AdBid> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, AdBid> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setHashKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new Jackson2JsonRedisSerializer<>(AdBid.class));
        tpl.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(AdBid.class));
        return tpl;
    }
}
