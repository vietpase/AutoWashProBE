package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.LoyaltyTierRequest;
import com.swp391.autowashpro.dto.LoyaltyTierResponse;
import com.swp391.autowashpro.service.LoyaltyTierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty-tiers")
@CrossOrigin("*")
@Tag(name = "Loyalty Tier Management", description = "APIs for configuring membership tiers, point multipliers, priority levels, and tier-specific discount rates")
public class LoyaltyTierController {

    private final LoyaltyTierService loyaltyTierService;

    public LoyaltyTierController(LoyaltyTierService loyaltyTierService) {
        this.loyaltyTierService = loyaltyTierService;
    }

    @GetMapping
    @Operation(
            summary = "Get all loyalty tiers (Manager view)",
            description = "Retrieve the full list of membership tiers including both active and inactive levels. Ordered by priority level."
    )
    public ResponseEntity<?> getAllTiers() {
        try {
            List<LoyaltyTierResponse> tiers = loyaltyTierService.getAllTiers();
            return ResponseEntity.ok(tiers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get active loyalty tiers(Customer view)",
            description = "Retrieve only the membership tiers that are currently active and running. Publicly accessible by customers to view benefits."
    )
    public ResponseEntity<?> getActiveTiers() {
        try {
            List<LoyaltyTierResponse> tiers = loyaltyTierService.getActiveTiers();
            return ResponseEntity.ok(tiers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Create a new loyalty tier (Manager Only)",
            description = "Allows managers to establish a new membership level with milestones (spending, visits), priorities, point multipliers, and discount rates."
    )
    public ResponseEntity<?> createTier(@Valid @RequestBody LoyaltyTierRequest request) {
        try {
            LoyaltyTierResponse createdTier = loyaltyTierService.createTier(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTier);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Update an existing loyalty tier (Manager Only)",
            description = "Allows managers to modify operational rules, point thresholds, booking restriction windows, or toggle active status for an existing tier by its ID."
    )
    public ResponseEntity<?> updateTier(@PathVariable("id") Integer id, @Valid @RequestBody LoyaltyTierRequest request) {
        try {
            LoyaltyTierResponse updatedTier = loyaltyTierService.updateTier(id, request);
            return ResponseEntity.ok(updatedTier);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Deactivate a loyalty tier (Manager Only)",
            description = "Performs a soft delete by turning the tier's status to inactive. This prevents system disruption for customers currently tied to this level."
    )
    public ResponseEntity<?> deleteTier(@PathVariable("id") Integer id) {
        try {
            loyaltyTierService.deleteTier(id);
            return ResponseEntity.ok("Loyalty tier with ID " + id + " has been deactivated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}