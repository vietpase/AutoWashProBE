package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.BookingRequest;
import com.swp391.autowashpro.dto.BookingResponse;
import com.swp391.autowashpro.dto.WalkInBookingRequest;
import com.swp391.autowashpro.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
@Tag(name = "Booking Processing Core", description = "Endpoints handling reservations, auto price calculation, tier snapshot freezing, voucher allocation validations, and counter-staff operational workflows.")
public class BookingController {

    private final BookingService bookingService;

    // Standard constructor injection matching your template template
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /* =========================================================================
     * CUSTOMER APP / WEB ENDPOINTS
     * ========================================================================= */

    @PostMapping("/create")
    @Operation(
            summary = "Place a new car wash reservation",
            description = "Creates a slot booking. Evaluates tier priority window, cross-checks vehicle ownership, calculates final price using tier dynamic pricing, promotion discounts, and checks multiple loyalty voucher limits."
    )
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* =========================================================================
     * COUNTER STAFF OPERATION ENDPOINTS
     * ========================================================================= */

    @PutMapping("/{id}/confirm-arrival")
    @Operation(
            summary = "Confirm vehicle arrival at the facility",
            description = "Updates the reservation status from 'Pending' to 'Confirmed' once the customer physically drives into the workshop queue."
    )
    public ResponseEntity<?> confirmArrival(@PathVariable Integer id) {
        try {
            BookingResponse response = bookingService.confirmArrival(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete-payment")
    @Operation(
            summary = "Finalize wash execution and process financial checkout",
            description = "Transitions the booking status to 'Completed'. Spawns formal WashHistory records, upgrades customer lifetime total spend/visits, allocates calculated reward points, logs loyalty variations, and securely recalculates customer aggregated monthly statistics."
    )
    public ResponseEntity<?> completeBookingAndPay(@PathVariable Integer id) {
        try {
            BookingResponse response = bookingService.completeBookingAndPay(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/walk-in")
    @Operation(
            summary = "Register an on-spot walk-in customer at the counter",
            description = "Handles immediate customer drive-ins. Performs real-time timeslot max-capacity checks, automatically provisions or fetches unmapped Customer/Vehicle profiles, computes member discounts, locks data snapshots, and pushes the queue directly to 'Confirmed'."
    )
    public ResponseEntity<?> createWalkInBooking(@Valid @RequestBody WalkInBookingRequest request) {
        try {
            BookingResponse response = bookingService.createWalkInBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}