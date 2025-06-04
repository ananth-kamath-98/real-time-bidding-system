package com.exploration.rtb.service;

import com.exploration.rtb.dto.AdBid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class AuctionResultPublisher {
    private final ValueOperations<String, AdBid> ops;

    public AuctionResultPublisher(RedisTemplate<String, AdBid> redisTemplate) {
        this.ops = redisTemplate.opsForValue();
    }

    public void publishResult(String impressionId, AdBid winner) {
        System.out.println("winner: " + winner.impressionId());
        ops.set(impressionId + " winner", winner);
    }
}