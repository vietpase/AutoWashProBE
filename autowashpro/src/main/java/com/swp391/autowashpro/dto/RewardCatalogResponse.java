package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.RewardCatalog;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RewardCatalogResponse {
    private Integer rewardId;
    private String rewardName;
    private String description;
    private Integer pointsRequired;
    private BigDecimal discountAmount;
    private Integer stockQuantity;
    private Boolean isActive;

    public RewardCatalogResponse(RewardCatalog reward) {
        this.rewardId = reward.getRewardId();
        this.rewardName = reward.getRewardName();
        this.description = reward.getDescription();
        this.pointsRequired = reward.getPointsRequired();
        this.discountAmount = reward.getDiscountAmount();
        this.stockQuantity = reward.getStockQuantity();
        this.isActive = reward.getIsActive();
    }
}