package com.exploration.rtb.dto;

public record WinnerAdBid(
  String adId,
  long   bidAmount,
  String timeSlot
) {}
