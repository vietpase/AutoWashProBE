package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "WashService")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WashService {
    @Id
    @Column(name = "service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @Column(name = "service_name", columnDefinition = "NVARCHAR(100)", nullable = false)
    private String serviceName;

    @Column(name = "service_type", length = 100, nullable = false)
    private String serviceType; // Car or Motorbike

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_minutes",nullable = false)
    private int durationMinutes;

    @Column(name = "is_active",nullable = false)
    private Boolean isActive=true;
}
