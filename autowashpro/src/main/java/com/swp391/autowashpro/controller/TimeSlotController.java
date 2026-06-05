package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.TimeSlotRequest;
import com.swp391.autowashpro.dto.TimeSlotResponse;
import com.swp391.autowashpro.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/time-slots")
@CrossOrigin("*")
@Tag(name = "Time Slot Management", description = "APIs for configuring operational working shifts and slot capacities")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService){
        this.timeSlotService = timeSlotService;
    }

    // Get all TimeSlots
    @GetMapping
    @Operation(summary = "Get all time slots", description = "Retrieve a full list of all time slots for both customers and staff to view.")
    public ResponseEntity<?> getAllSlots() {
        try {
            List<TimeSlotResponse> slots = timeSlotService.getAllTimeSlots();
            return ResponseEntity.ok(slots);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Create a new TimeSlot
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new time slot (Manager Only)", description = "Allows manager to onboard a new working shift. System validates duplicate name and time overlap.")
    public ResponseEntity<?> createSlot(@Valid @RequestBody TimeSlotRequest request) {
        try {
            TimeSlotResponse createdSlot = timeSlotService.createTimeSlot(request);
            return ResponseEntity.ok(createdSlot);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update an existing TimeSlot
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update an existing time slot (Manager Only)", description = "Allows manager to update slot details or toggle active status by its ID.")
    public ResponseEntity<?> updateSlot(
            @PathVariable("id") Integer id,
            @Valid @RequestBody TimeSlotRequest request
    ) {
        try {
            TimeSlotResponse updatedSlot = timeSlotService.updateTimeSlot(id, request);
            return ResponseEntity.ok(updatedSlot);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete an existing TimeSlot
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a time slot (Manager Only)", description = "Permanently remove a time slot. Chained bookings will block this action.")
    public ResponseEntity<?> deleteSlot(@PathVariable("id") Integer id) {
        try {
            timeSlotService.deleteTimeSlot(id);
            return ResponseEntity.ok("Time slot with ID " + id + " has been deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}