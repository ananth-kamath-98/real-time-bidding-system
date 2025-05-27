package com.exploration.rtb.component;

import com.exploration.rtb.dto.AdBid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class AuctionScheduler {

    private final BidBuffer buffer;
    private final RedisTemplate<String, AdBid> redis;

    public AuctionScheduler(BidBuffer buffer,
                            RedisTemplate<String, AdBid> redis) {
        this.buffer = buffer;
        this.redis = redis;
    }

    @Scheduled(fixedDelayString = "${rtb.slot-close-ms}")
    public void closeSlots() {
        var all = buffer.getAndClear();
        for (var entry : all.entrySet()) {
            String slot = entry.getKey();
            entry.getValue()
                    .stream()
                    .max(Comparator.comparingLong(AdBid::bidAmount))
                    .ifPresent(winner -> redis.opsForHash().put("winners", slot, winner));
        }
    }
}

