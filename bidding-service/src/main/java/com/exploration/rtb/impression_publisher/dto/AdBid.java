package com.exploration.rtb.impression_publisher.dto;

public record AdBid(
        String impressionId,
        String adId,
        long bidAmount
) {
}
