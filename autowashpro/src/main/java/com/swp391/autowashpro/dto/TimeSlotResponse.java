package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.TimeSlot;
import lombok.Data;

import java.time.LocalTime;
@Data
public class TimeSlotResponse {
    private Integer slotId;
    private String slotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxCapacity;
    private Boolean isStatus;

    public TimeSlotResponse (TimeSlot slot){
        this.slotId = slot.getSlotId();
        this.slotName = slot.getSlotName();
        this.startTime = slot.getStartTime();
        this.endTime = slot.getEndTime();
        this.maxCapacity = slot.getMaxCapacity();
        this.isStatus = slot.getIsActive();
    }
}
