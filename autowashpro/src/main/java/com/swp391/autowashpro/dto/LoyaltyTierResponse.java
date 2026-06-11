package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.LoyaltyTier;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class LoyaltyTierResponse {
    private Integer tierId;
    private String tierName;
    private BigDecimal minSpending;
    private Integer minVisits;
    private Integer bookingWindowDays;
    private Double pointMultiplier;
    private Integer priorityLevel;
    private Integer discountPercent;
    private Boolean isActive;

    public LoyaltyTierResponse(LoyaltyTier tier){
        this.tierId= tier.getTierId();
        this.tierName = tier.getTierName();
        this.minSpending = tier.getMinSpending();
        this.minVisits=tier.getMinVisits();
        this.bookingWindowDays=tier.getBookingWindowDays();
        this.pointMultiplier=tier.getPointMultiplier();
        this.priorityLevel=tier.getPriorityLevel();
        this.discountPercent=tier.getDiscountPercent();
        this.isActive = tier.getIsActive();
    }
}
