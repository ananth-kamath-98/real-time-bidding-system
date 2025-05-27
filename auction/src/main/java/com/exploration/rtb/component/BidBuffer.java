package com.exploration.rtb.component;

import com.exploration.rtb.dto.AdBid;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BidBuffer {

    private final Map<String, List<AdBid>> openBids = new ConcurrentHashMap<>();

    @RabbitListener(queues = "${rtb.routing-key}")
    public void receive(AdBid adBid){
        System.out.println("received");
        openBids.computeIfAbsent(adBid.timeSlot(), _ -> new ArrayList<>()).add(adBid);
    }

    public Map<String, List<AdBid>> getAndClear() {
        Map<String, List<AdBid>> snapshot = new HashMap<>(openBids);
        openBids.clear();
        return snapshot;
      }
}
