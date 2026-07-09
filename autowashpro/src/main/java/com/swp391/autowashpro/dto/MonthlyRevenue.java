package com.swp391.autowashpro.dto;

import java.math.BigDecimal;

public interface MonthlyRevenue {
    String getYearMonth();
    BigDecimal getTotalRevenue();
}