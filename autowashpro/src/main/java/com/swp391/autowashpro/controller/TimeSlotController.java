package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.TimeSlotResponse;
import com.swp391.autowashpro.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/admin/time-slots")
@CrossOrigin("*")
public class TimeSlotController {
    private TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService){
        this.timeSlotService = timeSlotService;
    }

//   Get all TimeSlots
    @GetMapping
    @Operation(summary = "Get all time slots")
    public ResponseEntity<List<TimeSlotResponse>> getAllTimeSlots(){
            return ResponseEntity.ok(timeSlotService.getAllTimeSlots());
    }
}
