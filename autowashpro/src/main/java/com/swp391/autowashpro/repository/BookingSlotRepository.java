package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.BookingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingSlotRepository extends JpaRepository<BookingSlot, Integer> {

    // Đếm xem tại ngày X, Ca Y có bao nhiêu xe đang nằm trong tiệm
    @Query("SELECT COUNT(bs) FROM BookingSlot bs " +
            "WHERE bs.booking.bookingDate = :date " +
            "AND bs.timeSlot.slotId = :slotId " +
            "AND bs.booking.status != 'Cancelled'")
    long countOccupiedVehicles(@Param("date") LocalDate date, @Param("slotId") Integer slotId);
}