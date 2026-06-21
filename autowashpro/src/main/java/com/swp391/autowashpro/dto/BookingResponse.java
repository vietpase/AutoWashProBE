package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Booking;
import com.swp391.autowashpro.entity.BookingSlot;
import com.swp391.autowashpro.entity.TimeSlot;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

@Data
public class BookingResponse {
    private Integer bookingId;
    private LocalDate bookingDate;
    private LocalDateTime createdAt;
    private Integer priorityLevel;
    private String status;
    private BigDecimal totalPrice;
    private String licensePlate;      // Biển số xe
    private String serviceName;       // Tên dịch vụ rửa
    private String tierNameAtBooking; // Hạng của khách lúc đặt lịch

    // --- HAI TRƯỜNG BỔ SUNG CHO FRONTEND DỄ HIỂN THỊ ---
    private String startSlotName;     // Tên ca bắt đầu (Ví dụ: Ca Sáng 1)
    private LocalTime startTime;      // Giờ bắt đầu rửa thực tế (Ví dụ: 08:00:00)

    public BookingResponse(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.bookingDate = booking.getBookingDate();
        this.createdAt = booking.getCreatedAt();
        this.priorityLevel = booking.getPriorityLevel();
        this.status = booking.getStatus();
        this.totalPrice = booking.getTotalPrice();

        // Map thông tin xe và dịch vụ
        if (booking.getVehicle() != null) {
            this.licensePlate = booking.getVehicle().getLicensePlate();
        }
        if (booking.getWashService() != null) {
            this.serviceName = booking.getWashService().getServiceName();
        }
        if (booking.getTierAtBooking() != null) {
            this.tierNameAtBooking = booking.getTierAtBooking().getTierName();
        }

        // --- Tự động tìm ca bắt đầu từ bảng trung gian ---
        if (booking.getBookingSlots() != null && !booking.getBookingSlots().isEmpty()) {
            booking.getBookingSlots().stream()
                    .map(BookingSlot::getTimeSlot)
                    // Tìm ca có giờ bắt đầu nhỏ nhất (sớm nhất) trong chuỗi các ca đã giữ chỗ
                    .min(Comparator.comparing(TimeSlot::getStartTime))
                    .ifPresent(startSlot -> {
                        this.startSlotName = startSlot.getSlotName();
                        this.startTime = startSlot.getStartTime();
                    });
        }
    }
}