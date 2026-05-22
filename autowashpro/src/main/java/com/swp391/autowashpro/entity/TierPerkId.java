package com.swp391.autowashpro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class TierPerkId implements Serializable {
    @Column(name = "tier_id")
    private int tierId;
    @Column(name = "perk_id")
    private int perkId;
}
