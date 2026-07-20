package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Booking;
import com.swp391.autowashpro.entity.BookingSlot;
import com.swp391.autowashpro.entity.TimeSlot;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Data
public class BookingListResponse {
    private Integer id;
    private String fullName;
    private String licensePlate;
    private String serviceName;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private BigDecimal totalPrice;


    public BookingListResponse(Booking booking){
        this.id = booking.getBookingId();
        this.fullName = booking.getVehicle().getCustomer().getFullName();
        this.licensePlate= booking.getLicensePlateAtBooking();
        this.serviceName=booking.getWashService().getServiceName();
        this.bookingDate=booking.getBookingDate();

        this.status=booking.getStatus();
        this.totalPrice=booking.getTotalPrice();
        // Logic lấy StartTime và EndTime
        if (booking.getBookingSlots() != null && !booking.getBookingSlots().isEmpty()) {
            // Lấy danh sách các TimeSlot từ bảng trung gian
            List<TimeSlot> slots = booking.getBookingSlots().stream()
                    .map(BookingSlot::getTimeSlot)
                    // Sắp xếp các slot theo thời gian bắt đầu tăng dần
                    .sorted(Comparator.comparing(TimeSlot::getStartTime))
                    .toList();

            // Lấy StartTime của ca đầu tiên
            this.startTime = slots.get(0).getStartTime();

            // Lấy EndTime của ca cuối cùng
            this.endTime = slots.get(slots.size() - 1).getEndTime();
        } else {
            // Trường hợp booking bị lỗi mất slot, gán null hoặc xử lý tùy ý
            this.startTime = null;
            this.endTime = null;
        }
    }
}
