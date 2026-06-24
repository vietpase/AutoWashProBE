package com.swp391.autowashpro.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class BookingDetailPriceResponse {
    private BigDecimal basePrice;
    private BigDecimal discountFromTier;
    private BigDecimal discountFromPromo;
    private BigDecimal discountFromReward;
    private String addOn;
    private Integer totalPointEarned;
    private BigDecimal finalPrice;

}
