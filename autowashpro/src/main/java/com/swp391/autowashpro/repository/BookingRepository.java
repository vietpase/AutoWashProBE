package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.dto.ServiceCustomer;
import com.swp391.autowashpro.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByCustomerAtBookingCustomerId(Integer customerId);

    @Query("SELECT COUNT(b) FROM Booking b")
    long countTotalBooking();

    long countByBookingDate(LocalDate date);

    //Bọc COALESCE để đảm bảo nếu bảng trống thì trả về 0, tránh lỗi Null
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b")
    BigDecimal getTotalRevenue();


    @Query("SELECT b.washService.serviceName AS serviceName, COUNT(b) AS numberCustomer FROM Booking b GROUP BY b.washService.serviceName")
    List<ServiceCustomer> countServiceCustomer();

}