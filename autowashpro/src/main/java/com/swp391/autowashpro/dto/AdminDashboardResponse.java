package com.swp391.autowashpro.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AdminDashboardResponse {
    long totalBooking;
    long todayBooking;
    BigDecimal totalRevenue;
    long newCustomers;
    long oldCustomers;
    long activePromotion;
    long activeReward;
    List<LoyaltyTierCustomer> loyaltyTierCustomers;
    List<ServiceCustomer> serviceCustomers;
    List<MonthlyRevenue> monthlyRevenues;

}
