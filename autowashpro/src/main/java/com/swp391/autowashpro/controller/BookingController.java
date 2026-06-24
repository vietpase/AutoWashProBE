package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@CrossOrigin(origins = "*")
@Tag(name = "Booking Processing Core", description = "Endpoints handling reservations, auto price calculation, tier snapshot freezing, voucher allocation validations, and counter-staff operational workflows.")
public class BookingController {

    private final BookingService bookingService;

    public BookingController (BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /* =========================================================================
     * PHẦN 1: ENDPOINTS DÀNH CHO GIAO DIỆN KHÁCH HÀNG (CUSTOMER SIDE)
     * ========================================================================= */

    @GetMapping("/available-slots")
    @Operation(
            summary = "Fetch dynamic available time slots",
            description = "Evaluates the entire timeline for the requested date and wash service duration. Identifies continuous slot chains and automatically disables starting slots that encounter mid-service capacity bottlenecks."
    )
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Integer washServiceId) {

        List<AvailableSlotResponse> slots = bookingService.getAvailableSlotsForCustomer(date, washServiceId);
        return ResponseEntity.ok(slots);
    }

    @PostMapping
    @Operation(
            summary = "Place a new car wash reservation",
            description = "Creates a slot booking. Evaluates tier priority window, cross-checks vehicle ownership, calculates final price using tier dynamic pricing, promotion discounts, and checks multiple loyalty voucher limits."
    )
    public ResponseEntity<BookingDetailPriceResponse> createOnlineBooking(@Valid @RequestBody BookingRequest request) {
        BookingDetailPriceResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /* =========================================================================
     * PHẦN 2: ENDPOINTS DÀNH CHO NHÂN VIÊN TIỆM (STAFF / OPERATOR SIDE)
     * ========================================================================= */

    @PostMapping("/walk-in")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(
            summary = "Create a walk-in booking at the counter",
            description = "Allows counter staff to instantly register walk-in customers and their vehicles. Bypasses advanced booking windows but strictly validates real-time capacity chains before forcing the booking into 'Confirmed' status."
    )
    public ResponseEntity<BookingDetailPriceResponse> createWalkInBooking(@Valid @RequestBody WalkInBookingRequest request) {
        BookingDetailPriceResponse response = bookingService.createWalkInBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{bookingId}/confirm-arrival")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(
            summary = "Confirm customer vehicle arrival",
            description = "Updates the booking status from 'Pending' to 'Confirmed' when the customer physically arrives at the shop, preparing the vehicle for the operational washing queue."
    )
    public ResponseEntity<BookingResponse> confirmCustomerArrival(@PathVariable Integer bookingId) {
        BookingResponse response = bookingService.confirmArrival(bookingId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookingId}/complete")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(
            summary = "Complete wash service and finalize payment settlement",
            description = "Finalizes the booking by shifting status to 'Completed'. Automatically creates wash history logs, calculates and triggers loyalty point accumulation based on dynamic tier multipliers, and updates customer monthly statistics."
    )
    public ResponseEntity<BookingResponse> completeBookingAndSettlement(@PathVariable Integer bookingId) {
        BookingResponse response = bookingService.completeBookingAndPay(bookingId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @Operation(
            summary = "Get booking List",
            description = "Get all bookings."
    )
    public ResponseEntity<?> getBookingList() {
        try{
            List<BookingListResponse> response= bookingService.getBookingList();
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    public ResponseEntity<?> cancelBooking(@PathVariable("id") Integer bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking has been cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}