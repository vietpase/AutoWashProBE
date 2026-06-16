package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.RedeemRequest;
import com.swp391.autowashpro.dto.RewardCatalogRequest;
import com.swp391.autowashpro.dto.RewardCatalogResponse;
import com.swp391.autowashpro.dto.RewardRedemptionResponse;
import com.swp391.autowashpro.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
@CrossOrigin("*")
@Tag(name = "Reward System Engine", description = "Endpoints for managing reward catalogs and handling customer loyalty points redemption history.")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Retrieve all reward items including inactive and out-of-stock items (Manager/Admin View)")
    public ResponseEntity<?> getAllRewardsForAdmin() {
        return ResponseEntity.ok(rewardService.getAllRewardsForAdmin());
    }

    @GetMapping("/customer/catalog")
    @Operation(summary = "Get list of available rewards for customers to redeem (Active & In-stock)")
    public ResponseEntity<?> getActiveRewardsForCustomer() {
        return ResponseEntity.ok(rewardService.getActiveRewardsForCustomer());
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new reward item in the system catalog (Manager Only)")
    public ResponseEntity<?> createReward(@Valid @RequestBody RewardCatalogRequest request) {
        RewardCatalogResponse response = rewardService.createReward(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update reward details or replenish stock quantity (Manager Only)")
    public ResponseEntity<?> updateReward(@PathVariable("id") Integer id, @Valid @RequestBody RewardCatalogRequest request) {
        return ResponseEntity.ok(rewardService.updateReward(id, request));
    }

    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Soft delete / Deactivate a reward item from the catalog (Manager Only)")
    public ResponseEntity<?> deleteReward(@PathVariable("id") Integer id) {
        rewardService.deleteRewardSoft(id);
        return ResponseEntity.ok("Deactivated reward item success.");
    }

    @PostMapping("/customer/redeem")
    @Operation(summary = "Process customer loyalty points redemption for vouchers or rewards")
    public ResponseEntity<?> redeemRewardPoints(@Valid @RequestBody RedeemRequest request) {
        try {
            RewardRedemptionResponse response = rewardService.redeemRewardPoints(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customer/history/{customerId}")
    @Operation(summary = "Retrieve the complete point redemption history of a specific customer")
    public ResponseEntity<?> getCustomerRedemptionHistory(@PathVariable("customerId") Integer customerId) {
        return ResponseEntity.ok(rewardService.getCustomerRedemptionHistory(customerId));
    }

    @GetMapping("/customer/unused/{customerId}")
    @Operation(summary = "Get list of redeemed vouchers that have NOT been used yet (Available for checkout)")
    public ResponseEntity<?> getAvailableVouchers(@PathVariable("customerId") Integer customerId) {
        return ResponseEntity.ok(rewardService.getAvailableVouchersForCustomer(customerId));
    }
}