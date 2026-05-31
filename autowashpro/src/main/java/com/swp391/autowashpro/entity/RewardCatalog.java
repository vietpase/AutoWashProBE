package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "RewardCatalog")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RewardCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id")
    private Integer rewardId;

    @Column(name = "reward_name", nullable = false, length = 100)
    private String rewardName;

    @Column(name = "points_required", nullable = false)
    private Integer pointsRequired;

    @Column(name = "discount_amount", precision = 18,scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "free_wash")
    private Boolean freeWash = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_admin_id")
    private AdminAccount createdByAdmin;

}
