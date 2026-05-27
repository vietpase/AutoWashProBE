package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Booking")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @Column(name = "booking_date",nullable = false)
    private LocalDate bookingDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();

    @Column(name = "priority_level",nullable = false)
    private Integer priorityLevel = 1;

    @Column(name = "status", length = 30, nullable = false)
    private String status = "Pending";

    @Column(name = "total_price",nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id",nullable = false)
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private WashService washService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id_at_booking", nullable = false)
    private LoyaltyTier tierAtBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by_admin_id")
    private AdminAccount cancelledByAdmin;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RewardRedemption> rewardRedemptions;
}
