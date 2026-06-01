package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.PromotionRequest;
import com.swp391.autowashpro.dto.PromotionResponse;
import com.swp391.autowashpro.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "*")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService){
        this.promotionService=promotionService;
    }

//  Get all promotions
    @GetMapping
    @Operation(summary = "Get all promotions")
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        List<PromotionResponse> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }
//  Create a promotion(Admin only)
    @PostMapping
    @Operation(summary = "Create a new promotion (Admin Only)")
    public ResponseEntity<PromotionResponse> createPromotion(@Valid @RequestBody PromotionRequest request) {
        PromotionResponse createdPromotion = promotionService.createPromotion(request);
        return ResponseEntity.ok(createdPromotion);
    }

//  Update a promotion by Id(Admin only)
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing promotion (Admin Only)")
    public ResponseEntity<PromotionResponse> updatePromotion(@Valid @PathVariable Integer promoId,
                                             @RequestBody PromotionRequest promotionRequest){
        PromotionResponse promotionResponse = promotionService.updatePromotion(promoId,promotionRequest);
        return ResponseEntity.ok(promotionResponse);
    }
//  Delete a promotion by Id(Admin only)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a promotion (Admin Only)")
    public ResponseEntity<String> deletePromotion(@PathVariable Integer id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok("Promotion with ID " + id + " has been deleted successfully!");
    }

}
