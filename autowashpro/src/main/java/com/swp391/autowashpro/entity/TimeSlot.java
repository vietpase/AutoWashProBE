package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalTime;
import java.util.List;

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
    private Integer slotId;

    @Column(name = "slot_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String slotName;

    @Column(name = "start_time", nullable = false, columnDefinition = "time")
    @JdbcTypeCode(Types.TIME)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "time")
    @JdbcTypeCode(Types.TIME)
    private LocalTime endTime;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
    private List<BookingSlot> bookingSlots;
}