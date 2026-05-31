package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class PromotionRequest {
    @NotBlank(message = "Promo code cannot be blank")
    private String promoCode;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Discount amount cannot be null")
    @Positive(message = "Discount amount must be greater than 0")
    private Double discountAmount;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    private Integer minTierId;
}
