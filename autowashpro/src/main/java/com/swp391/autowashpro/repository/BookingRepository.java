package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Đếm những đơn KHÁC trạng thái 'Cancelled'
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingDate = :date AND b.timeSlot.slotId = :slotId AND b.status != 'Cancelled'")
    long countActiveBookings(@Param("date") LocalDate date, @Param("slotId") Integer slotId);
}