package com.exploration.rtb.dto;

public record WinnerAdBid(
  String impressionId,
  String adId,
  long   bidAmount
) {}
