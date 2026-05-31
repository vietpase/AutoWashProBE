package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.VehicleRequest;
import com.swp391.autowashpro.dto.VehicleResponse;
import com.swp391.autowashpro.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Retrieve a list of vehicles belonging to a specific customer
    @GetMapping("/customer")
    @Operation(summary = "Get customer vehicle list")
    public ResponseEntity<?> getMyVehicle(@RequestParam int customerId) {
        try {
            List<VehicleResponse> vehicles = vehicleService.getMyVehicles(customerId);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Add a new vehicle to the system
    @PostMapping
    @Operation(summary = "Add a new Vehicle")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleRequest request) {
        try {
            VehicleResponse savedVehicle = vehicleService.addVehicle(request);
            return ResponseEntity.ok(savedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update existing vehicle details by its ID
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle by ID")
    public ResponseEntity<?> updateVehicle(@PathVariable("id") Integer vehicleId, @RequestBody VehicleRequest request) {
        try {
            VehicleResponse vehicleResponse = vehicleService.updateVehicle(vehicleId, request);
            return ResponseEntity.ok(vehicleResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete a specific vehicle from the system by its ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Vehicle by ID")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") int vehicleId) {
        try {
            vehicleService.deleteVehicle(vehicleId);
            return ResponseEntity.ok("Vehicle deleted successfully with ID: " + vehicleId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}