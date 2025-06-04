package com.exploration.rtb.impression_publisher.service;

import com.exploration.rtb.impression_publisher.dto.AdBid;
import com.exploration.rtb.impression_publisher.dto.ImpressionRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ImpressionListener {

    private final RabbitTemplate rabbit;
    private final String bidsExchange;
    private final String bidsRoutingKey;

    public ImpressionListener(RabbitTemplate rabbit,
                              @Value("${rtb.exchange.bids}") String bidsExchange,
                              @Value("${rtb.routing.bids}") String bidsRoutingKey) {
        this.rabbit = rabbit;
        this.bidsExchange = bidsExchange;
        this.bidsRoutingKey = bidsRoutingKey;
    }

    @RabbitListener(queues = "${rtb.queue.impressions}")
    public void handleImpression(ImpressionRequest req) {
        var bidAmount =
                req.floor() + ThreadLocalRandom.current().nextLong(req.floor() + 1);
        var adId = UUID.randomUUID().toString();

        var bid = new AdBid(req.impressionId(), adId, bidAmount);
        System.out.println("[Bidder] Sending bid=" + bidAmount + " for impressionId=" + req.impressionId());
        rabbit.convertAndSend(bidsExchange, bidsRoutingKey, bid);
    }
}
