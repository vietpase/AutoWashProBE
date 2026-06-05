package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.PromotionRequest;
import com.swp391.autowashpro.dto.PromotionResponse;
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

    // Get all promotions
    @GetMapping
    @Operation(summary = "Get all promotions", description = "Retrieve a full list of all available promotions. Open for both customers and staff to view.")
    public ResponseEntity<?> getAllPromotions() {
        try {
            List<PromotionResponse> promotions = promotionService.getAllPromotions();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Create a new promotion
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new promotion (Manager Only)", description = "Allows manager to onboard a new marketing campaign or discount event.")
    public ResponseEntity<?> createPromotion(@Valid @RequestBody PromotionRequest request) {
        try {
            PromotionResponse createdPromotion = promotionService.createPromotion(request);
            // Trả về trạng thái 201 Created giống như cấu hình cũ của ông
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPromotion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update an existing promotion
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

    // Delete a promotion
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a promotion (Manager Only)", description = "Permanently remove a promotion campaign from the system.")
    public ResponseEntity<?> deletePromotion(@PathVariable("id") Integer id) {
        try {
            promotionService.deletePromotion(id);
            return ResponseEntity.ok("Promotion with ID " + id + " has been deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}