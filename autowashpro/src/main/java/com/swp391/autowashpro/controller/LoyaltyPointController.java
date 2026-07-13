package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.service.LoyaltyPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty-points")
@CrossOrigin("*")
@Tag(name = "Loyalty Points ", description = "Endpoints for managing loyalty points.")
public class LoyaltyPointController {
    private final LoyaltyPointService loyaltyPointService;

    public LoyaltyPointController(LoyaltyPointService loyaltyPointService){
        this.loyaltyPointService=loyaltyPointService;
    }

    @GetMapping("/customer/{id}")
    @Operation(summary = "Get list of point transactions for customers")
    public ResponseEntity<?> getPointTransaction(@PathVariable("id") Integer customerId) {
        return ResponseEntity.ok(loyaltyPointService.getPointTransaction(customerId));
    }
}
