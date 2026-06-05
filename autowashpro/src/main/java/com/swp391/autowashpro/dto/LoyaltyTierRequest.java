package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class LoyaltyTierRequest {
    @NotBlank(message = "Tier name cannot be blank")
    private String tierName;

    @NotNull(message = "Minimum spending is required")
    @Min(value = 0, message = "Minimum spending cannot be negative")
    private BigDecimal minSpending;

    @NotNull(message = "Minimum visits is required")
    @Min(value = 0, message = "Minimum visits cannot be negative")
    private Integer minVisits;

    @NotNull(message = "Booking window days is required")
    @Min(value = 1, message = "Booking window must be at least 1 day")
    private Integer bookingWindowDays;

    @NotNull(message = "Point multiplier is required")
    @Min(value = 1, message = "Point multiplier must be at least 1.0")
    private Double pointMultiplier;

    @NotNull(message = "Priority level is required")
    @Min(value = 1, message = "Priority level must be at least 1")
    private Integer priorityLevel;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount percentage cannot be negative")
    private Integer discountPercent;

    private Boolean isActive;
}
