package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Customer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerResponse {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String loyaltyTier;
    private Integer currentPoints;
    private Integer totalVisits;
    private BigDecimal totalSpend;

    public CustomerResponse(Customer customer){
        this.fullName= customer.getFullName();
        this.phoneNumber= customer.getPhoneNumber();
        this.email= customer.getEmail();
        this.loyaltyTier=customer.getLoyaltyTier().getTierName();
        this.currentPoints=customer.getCurrentPoints();
        this.totalVisits=customer.getTotalVisits();
        this.totalSpend=customer.getTotalSpend();
    }
}
