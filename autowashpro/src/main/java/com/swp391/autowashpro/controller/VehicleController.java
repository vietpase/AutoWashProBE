package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.VehicleRequest;
import com.swp391.autowashpro.dto.VehicleResponse;
import com.swp391.autowashpro.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
@Tag(name = "Vehicle Portfolio Management", description = "APIs for tracking and managing user garage assets, license plates, and vehicle classification metadata")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Get customer vehicle list",
            description = "Returns a complete collection of all registered cars/motorbikes assigned to a specific customer's garage portfolio."
    )
    public ResponseEntity<?> getMyVehicle(@RequestParam Integer customerId) {
        try {
            List<VehicleResponse> vehicles = vehicleService.getMyVehicles(customerId);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Add a new vehicle",
            description = "Attaches a new vehicle asset (plate number, type, brand) directly into the owning customer's active account portfolio."
    )
    public ResponseEntity<?> addVehicle(@Valid @RequestBody VehicleRequest request) {
        try {
            VehicleResponse savedVehicle = vehicleService.addVehicle(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle); // Chuẩn hóa thành 201 Created
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Update vehicle by ID",
            description = "Modifies the structural properties, registration plates, or type classification info of an existing target vehicle asset."
    )
    public ResponseEntity<?> updateVehicle(@PathVariable("id") Integer id, @Valid @RequestBody VehicleRequest request) {
        try {
            // Sửa tên biến PathVariable từ vehicleId -> id cho đồng bộ tuyệt đối với biểu thức đường dẫn /{id}
            VehicleResponse vehicleResponse = vehicleService.updateVehicle(id, request);
            return ResponseEntity.ok(vehicleResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(
            summary = "Delete vehicle by ID",
            description = "Decommissions a customer's vehicle asset from the system view. Historical dependent order logs may lock this operation from physical hard deletion."
    )
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") Integer id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.ok("Vehicle deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}