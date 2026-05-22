package com.swp391.autowashpro.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Perk")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Perk {
    @Id
    @Column(name = "perk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int perkId;

    @Column(name = "perk_name", length = 100,nullable = false)
    private String perkName;

    @Column(name = "discount_percent")
    private Double discountPercent=0.0;

    @Column(name = "free_service",length = 100)
    private String freeService;

    @Column(name = "add_on_item",length = 100)
    private String addOnItem;

}
