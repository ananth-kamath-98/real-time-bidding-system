package com.exploration.rtb.impression_publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ImpressionRequest (
        @NotBlank String impressionId,
        @Positive long floor
){
}
