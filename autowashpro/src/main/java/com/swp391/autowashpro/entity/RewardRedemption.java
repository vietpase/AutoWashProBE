package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RewardRedemption")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RewardRedemption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redemption_id")
    private Integer redemptionId;

    @Column(name = "points_used", nullable = false)
    private Integer pointsUsed;

    @Column(name = "redemption_date")
    private LocalDateTime redemptionDate = LocalDateTime.now();

    // --- SNAPSHOT FIELDS (ĐÓNG BĂNG DỮ LIỆU LÚC ĐỔI QUÀ) ---
    @Column(name = "reward_name_at_redemption", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String rewardNameAtRedemption;

    @Column(name = "discount_amount_at_redemption", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountAmountAtRedemption;
    // -----------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", nullable = false)
    private RewardCatalog rewardCatalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id") // Cho phép NULL khi chưa áp dụng vào đơn đặt lịch
    private Booking booking;
}