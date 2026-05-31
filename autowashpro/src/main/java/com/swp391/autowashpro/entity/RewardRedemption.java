package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import jakarta.validation.GroupSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "points_used",nullable = false)
    private Integer pointsUsed;

    @Column(name = "redemption_date")
    private LocalDateTime redemptionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", nullable = false)
    private RewardCatalog rewardCatalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
