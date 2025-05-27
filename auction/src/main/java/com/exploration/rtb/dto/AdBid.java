package com.exploration.rtb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AdBid(
  @NotBlank String adId,
  @Min(1) long   bidAmount,
  @NotBlank String timeSlot
) { }