package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface BookingRepository extends JpaRepository<Booking, Integer> {


    @Query("SELECT COUNT(b) FROM Booking b " +
            "JOIN b.bookingSlots bs " + // JOIN sang danh sách bảng trung gian
            "WHERE b.bookingDate = :date " +
            "AND bs.timeSlot.slotId = :slotId " + // Tìm thông qua alias của bảng trung gian
            "AND b.status != 'CANCELLED'") // Viết hoa chữ CANCELLED để khớp với Enum/String
    long countBookingsByDateAndSlotId(@Param("date") LocalDate date, @Param("slotId") Integer slotId);
}