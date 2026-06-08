package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RewardCatalogRequest {
    @NotBlank(message = "Reward name cannot be blank")
    private String rewardName;

    private String description;

    @NotNull(message = "Points required is mandatory")
    @Min(value = 1, message = "Points required must be at least 1")
    private Integer pointsRequired;

    @NotNull(message = "Discount amount is required")
    @Min(value = 0, message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private Boolean isActive;
}