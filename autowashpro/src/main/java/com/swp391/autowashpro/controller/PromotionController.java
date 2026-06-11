package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.PromotionRequest;
import com.swp391.autowashpro.dto.PromotionResponse;
import com.swp391.autowashpro.entity.Promotion;
import com.swp391.autowashpro.repository.PromotionRepository;
import com.swp391.autowashpro.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "*")
@Tag(name = "Promotion Management", description = "APIs for creating and managing discounts, marketing campaigns, and membership tier promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get all promotions (Manager Only)", description = "Retrieve a full list of all available promotions including inactive ones.")
    public ResponseEntity<?> getAllPromotions() {
        try {
            List<PromotionResponse> promotions = promotionService.getAllPromotions();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    //@PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'MANAGER')")
    @Operation(summary = "Get active promotions for customers(Customer, Staff view)", description = "Retrieve a list of promotions that are currently active and valid.")
    public ResponseEntity<?> getActivePromotions() {
        try {
            List<PromotionResponse> promotions = promotionService.getActivePromotions();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new promotion (Manager Only)", description = "Allows manager to onboard a new marketing campaign or discount event.")
    public ResponseEntity<?> createPromotion(@Valid @RequestBody PromotionRequest request) {
        try {
            PromotionResponse createdPromotion = promotionService.createPromotion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPromotion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update an existing promotion (Manager Only)", description = "Allows manager to modify information or change duration/conditions of a promotion by its ID.")
    public ResponseEntity<?> updatePromotion(
            @PathVariable("id") Integer id,
            @Valid @RequestBody PromotionRequest promotionRequest
    ) {
        try {
            PromotionResponse promotionResponse = promotionService.updatePromotion(id, promotionRequest);
            return ResponseEntity.ok(promotionResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Deactivate a promotion (Manager Only)", description = "Deactivate a promotion campaign by setting its active status to false to preserve historical logs.")
    public ResponseEntity<?> deletePromotion(@PathVariable("id") Integer id) {
        try {
            promotionService.deactivatePromotion(id);
            return ResponseEntity.ok("Promotion deactivated successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}