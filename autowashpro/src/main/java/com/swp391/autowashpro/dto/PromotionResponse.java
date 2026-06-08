package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Promotion;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PromotionResponse {
    private Integer promoId;
    private String promoName;
    private String description;
    private Double discountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive; // Đã đổi từ String status sang Boolean isActive
    private Integer minTierId;
    private String minTierName;

    public PromotionResponse(Promotion promo) {
        this.promoId = promo.getPromoId();
        this.promoName = promo.getPromoName();
        this.description = promo.getDescription();
        this.discountAmount = promo.getDiscountAmount();
        this.startDate = promo.getStartDate();
        this.endDate = promo.getEndDate();
        this.isActive = promo.getIsActive();

        if (promo.getLoyaltyTier() != null) {
            this.minTierId = promo.getLoyaltyTier().getTierId();
            this.minTierName = promo.getLoyaltyTier().getTierName();
        } else {
            this.minTierName = "All Tiers";
        }
    }
}