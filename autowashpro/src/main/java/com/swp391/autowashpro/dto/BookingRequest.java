package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookingRequest {
    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Vehicle ID is required")
    private Integer vehicleId;

    @NotNull(message = "Time Slot ID is required")
    private Integer slotId;

    @NotNull(message = "Wash Service ID is required")
    private Integer washServiceId;

    private LocalDate bookingDate; // Ngày khách đến rửa xe
    private Integer promotionId;   // Mã giảm giá marketing (nếu có)
    private List<Integer> appliedRedemptionIds; // Danh sách ID Voucher đổi thưởng khách chọn áp dụng
}