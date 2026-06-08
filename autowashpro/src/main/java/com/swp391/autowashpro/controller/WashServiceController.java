package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.WashServiceRequest;
import com.swp391.autowashpro.dto.WashServiceResponse;
import com.swp391.autowashpro.service.WashServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/wash-services")
@CrossOrigin("*")
@Tag(name = "Wash Service Management", description = "APIs for configuring wash service packages, pricing, and execution durations")
public class WashServiceController {

    private final WashServiceService washServiceService;

    public WashServiceController(WashServiceService washServiceService) {
        this.washServiceService = washServiceService;
    }

    @GetMapping
    @Operation(
            summary = "Get all wash services",
            description = "Retrieve a full list of all wash service packages available in the system. Accessible by customers, staff, and managers."
    )
    public ResponseEntity<?> getAll(){
        try {
            List<WashServiceResponse> services = washServiceService.getAllWashServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active wash services for customers",
            description = "Retrieve a list of wash service packages that are currently active and available for booking."
    )
    public ResponseEntity<?> getActiveServices() {
        try {
            List<WashServiceResponse> services = washServiceService.getActiveWashServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Create a new wash service (Manager Only)",
            description = "Allows a manager to onboard a new wash service package by defining its name, price, and required duration."
    )
    public ResponseEntity<?> create(@Valid @RequestBody WashServiceRequest request) {
        try {
            WashServiceResponse createdService = washServiceService.createService(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Update an existing wash service (Manager Only)",
            description = "Allows a manager to modify the details, adjust pricing, or update the operational duration of an existing wash service by its ID."
    )
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody WashServiceRequest request) {
        try {
            WashServiceResponse updatedService = washServiceService.updateService(id, request);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Soft delete / Deactivate a wash service (Manager Only)",
            description = "Deactivate a wash service package by setting its active status to false."
    )
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            washServiceService.deactivateService(id);

            return ResponseEntity.ok("Wash service deactivated successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}