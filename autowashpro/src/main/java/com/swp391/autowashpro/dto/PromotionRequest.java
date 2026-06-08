package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PromotionRequest {
    @NotBlank(message = "Promo name cannot be blank")
    private String promoName;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Discount amount cannot be null")
    private Double discountAmount;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    private Boolean isActive;

    @Min(value = 1, message = "Minimum tier ID must be at least 1")
    private Integer minTierId;
}