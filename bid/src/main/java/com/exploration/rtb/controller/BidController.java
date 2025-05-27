package com.exploration.rtb.controller;

import com.exploration.rtb.dto.AdBid;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/bids")
public class BidController {

    private final RabbitTemplate rabbit;
    private final String exchange;
    private final String routingKey;

    public BidController(RabbitTemplate rabbit,
                         @Value("${rtb.exchange}") String exchange,
                         @Value("${rtb.routing-key}") String routingKey) {
        this.rabbit = rabbit;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> submitBid(@RequestBody @Valid AdBid bid) {
        rabbit.convertAndSend(exchange, routingKey, bid);
        return ResponseEntity.accepted().build();
    }
}
