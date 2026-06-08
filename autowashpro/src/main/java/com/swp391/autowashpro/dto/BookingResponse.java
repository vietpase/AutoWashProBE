package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Booking;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponse {
    private Integer bookingId;
    private LocalDate bookingDate;
    private LocalDateTime createdAt;
    private Integer priorityLevel;
    private String status;
    private BigDecimal totalPrice;
    private String licensePlate;   // Biển số xe
    private String slotName;       // Tên khung giờ (VD: 08:00 - 09:00)
    private String serviceName;    // Tên dịch vụ rửa
    private String tierNameAtBooking; // Hạng của khách lúc đặt lịch

    public BookingResponse(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.bookingDate = booking.getBookingDate();
        this.createdAt = booking.getCreatedAt();
        this.priorityLevel = booking.getPriorityLevel();
        this.status = booking.getStatus();
        this.totalPrice = booking.getTotalPrice();
        this.licensePlate = booking.getVehicle().getLicensePlate();
        this.slotName = booking.getTimeSlot().getSlotName();
        this.serviceName = booking.getWashService().getServiceName();
        this.tierNameAtBooking = booking.getTierAtBooking().getTierName();
    }
}