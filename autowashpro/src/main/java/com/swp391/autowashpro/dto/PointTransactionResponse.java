package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.LoyaltyPoint;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PointTransactionResponse {
    private LocalDate createAt;
    private Integer pointsChange;
    private String transactionType;

    public PointTransactionResponse(LoyaltyPoint loyaltyPoint){
        this.createAt=loyaltyPoint.getCreatedAt().toLocalDate();
        this.pointsChange= loyaltyPoint.getPointsChange();
        this.transactionType= loyaltyPoint.getTransactionType();
    }
}
