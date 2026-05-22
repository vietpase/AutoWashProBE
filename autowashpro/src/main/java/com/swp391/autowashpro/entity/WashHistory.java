package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "WashHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WashHistory {
    @Id
    @Column(name = "wash_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int washId;

    @Column(name = "wash_date")
    private LocalDateTime washDate;

    @Column(name = "amount_paid", precision = 18, scale = 2, nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "points_earned")
    private Integer pointsEarned = 0;

    @Column(name = "points_used")
    private Integer pointsUsed = 0;

    @Column(name = "perk_applied", length = 200)
    private String perkApplied;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;
}
