package com.exploration.rtb.dto;

public record AdBid(
    String impressionId,
    String adId,
    long bidAmount
) {}