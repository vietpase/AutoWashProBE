package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Booking;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class BookingListResponse {
    private Integer id;
    private String fullName;
    private String licensePlate;
    private String serviceName;
    private LocalDate bookingDate;
    private LocalTime createdAt;
    private String status;
    private BigDecimal totalPrice;


    public BookingListResponse(Booking booking){
        this.id = booking.getBookingId();
        this.fullName = booking.getVehicle().getCustomer().getFullName();
        this.licensePlate= booking.getLicensePlateAtBooking();
        this.serviceName=booking.getWashService().getServiceName();
        this.bookingDate=booking.getBookingDate();
        this.createdAt=booking.getCreatedAt().toLocalTime();
        this.status=booking.getStatus();
        this.totalPrice=booking.getBasePriceAtBooking();
    }
}
