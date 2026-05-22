package com.swp391.autowashpro.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Customer")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(15)", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "email", columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "current_points")
    private int currentPoints = 0;

    @Column(name = "total_spend", precision = 18, scale = 2)
    private BigDecimal totalSpend = BigDecimal.ZERO;

    @Column(name = "total_visits")
    private int totalVisits = 0;

    @Column(name = "last_tier_review")
    private Date lastTierReview;

    @Column(name = "create_at", updatable = false)
    private LocalDate createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private LoyaltyTier loyaltyTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_updated_by_admin_id")
    private AdminAccount adminAccount;

    @PrePersist
    protected void onCreate() {
        if (this.createAt == null) {
            this.createAt = LocalDate.now();
        }
        if (this.loyaltyTier == null) {
            this.loyaltyTier = new LoyaltyTier(1); // Safely sets default ID only when creating a completely new entity
        }
    }
}
