package com.exploration.rtb.impression_publisher.controller;

import com.exploration.rtb.impression_publisher.dto.ImpressionRequest;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/impressions")
public class ImpressionController {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;

    public ImpressionController(
            RabbitTemplate rabbitTemplate,
            @Value("${rtb.exchange.impressions}") String exchange
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @PostMapping
    public ResponseEntity<Void> newImpression(@RequestBody @Valid ImpressionRequest req) {
        System.out.println("Sending impression request: " + req);
        rabbitTemplate.convertAndSend(exchange, "", req);
        return ResponseEntity.accepted().build();
    }
}
