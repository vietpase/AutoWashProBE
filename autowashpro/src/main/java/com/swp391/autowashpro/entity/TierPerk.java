package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TierPerk")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TierPerk {
    @EmbeddedId
    private TierPerkId id;

    @ManyToOne
    @MapsId("tierId")
    @JoinColumn(name = "tier_id")
    private LoyaltyTier loyaltyTier;

    @ManyToOne
    @MapsId("perkId")
    @JoinColumn(name = "perk_id")
    private Perk perk;
}
