package com.swp391.autowashpro.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class AvailableSlotResponse {
    private Integer slotId;
    private String slotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxCapacity;
    private boolean isAvailable; // Trường quyết định nút bấm sáng hay xám trên UI
}