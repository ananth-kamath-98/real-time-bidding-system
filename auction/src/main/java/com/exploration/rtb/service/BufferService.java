package com.exploration.rtb.service;

import com.exploration.rtb.dto.AdBid;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class BufferService {
    private final Map<String, List<AdBid>> bidBuffers = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final long auctionDelayMs;
    private final AuctionResultPublisher resultPublisher;

    public BufferService(@Value("${rtb.auction.delay-ms}") long auctionDelayMs,
                               AuctionResultPublisher resultPublisher) {
        this.auctionDelayMs = auctionDelayMs;
        this.resultPublisher = resultPublisher;
    }

    @RabbitListener(queues = "${rtb.queue.bids}")
    public void receiveBid(AdBid bid) {
        bidBuffers.computeIfAbsent(bid.impressionId(), id -> {
            scheduleAuction(id);
            return new CopyOnWriteArrayList<>();
        }).add(bid);
    }

    private void scheduleAuction(String impressionId) {
        Runnable task = () -> {
            List<AdBid> bids = bidBuffers.remove(impressionId);
            if (bids != null && !bids.isEmpty()) {
                AdBid winner = bids.stream()
                        .max(Comparator.comparingLong(AdBid::bidAmount))
                        .get();
                resultPublisher.publishResult(impressionId, winner);
            }
            timers.remove(impressionId);
        };
        var future = scheduler.schedule(task, auctionDelayMs,
                TimeUnit.MILLISECONDS);
        timers.put(impressionId, future);
    }
}

