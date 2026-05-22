package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Vehicle")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicleId;

    @Column(name = "license_plate",columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "vehicle_type", length = 50)
    private String vehicleType;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "color", length = 30)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

}
