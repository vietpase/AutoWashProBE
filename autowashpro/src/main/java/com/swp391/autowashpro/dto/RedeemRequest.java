package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RedeemRequest {
    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Reward ID is required")
    private Integer rewardId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}