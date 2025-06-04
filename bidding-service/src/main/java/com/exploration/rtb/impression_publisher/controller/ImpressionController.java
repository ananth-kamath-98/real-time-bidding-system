//package com.exploration.rtb.impression_publisher.controller;
//
//import com.exploration.rtb.impression_publisher.dto.ImpressionRequest;
//import jakarta.validation.Valid;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/impressions")
//public class ImpressionController {
//
//    private final RabbitTemplate rabbitTemplate;
//    private final String exchange;
//    private final String routingKey;
//
//    public ImpressionController(
//            RabbitTemplate rabbitTemplate,
//            @Value("${rtb.exchange.impressions}") String exchange,
//            @Value("${rtb.routing.impressions}") String routingKey
//    ) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.exchange = exchange;
//        this.routingKey = routingKey;
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> newImpression(@RequestBody @Valid ImpressionRequest req) {
//        rabbitTemplate.convertAndSend(exchange, routingKey, req);
//        return ResponseEntity.accepted().build();
//    }
//}
