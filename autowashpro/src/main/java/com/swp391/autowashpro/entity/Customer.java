package com.swp391.autowashpro.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})//Clear Hibernate Proxy junk files
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    @Column(name = "full_name", columnDefinition = "NVARCHAR(100)", nullable = false)
    private String fullName;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(15)", unique = true)
    private String phoneNumber;

    @Column(name = "email", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    @JsonIgnore //Jackson will ignore this field when converting to JSON.
    private String password;

    @Column(name = "current_points")
    private Integer currentPoints = 0;

    @Column(name = "total_spend", precision = 18, scale = 2)
    private BigDecimal totalSpend = BigDecimal.ZERO;

    @Column(name = "total_visits")
    private Integer totalVisits = 0;

    @Column(name = "last_tier_review")
    private Date lastTierReview;

    @Column(name = "create_at", updatable = false)
    private LocalDate createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private LoyaltyTier loyaltyTier;

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
