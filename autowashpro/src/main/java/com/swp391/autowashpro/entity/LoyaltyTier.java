package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "LoyaltyTier")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoyaltyTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Integer tierId;

    @Column(name = "tier_name",length = 50,nullable = false)
    private String tierName;

    @Column(name = "min_spending",precision = 18,scale = 2)
    private BigDecimal minSpending = BigDecimal.ZERO;

    @Column(name = "min_visits")
    private Integer minVisits= 0;

    @Column(name = "booking_window_days",nullable = false)
    private Integer bookingWindowDays;

    @Column(name = "point_multiplier")
    private Double pointMultiplier = 1.0;

    @Column(name = "priority_level", nullable = false)
    private Integer priorityLevel;

    @Column(name = "discount_percent")
    private Integer discountPercent = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive=true;

    public LoyaltyTier(int tierID){
        this.tierId = tierID;
    }
}
