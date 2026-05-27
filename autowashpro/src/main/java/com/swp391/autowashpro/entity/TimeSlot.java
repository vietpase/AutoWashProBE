package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "TimeSlot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @Id
    @Column(name="slot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotId;

    @Column(name = "slot_name",nullable = false)
    private String slotName;

    @Column(name = "start_time",nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time",nullable = false)
    private LocalTime endTime;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "is_active",nullable = false)
    private Boolean isActive=true;
}
