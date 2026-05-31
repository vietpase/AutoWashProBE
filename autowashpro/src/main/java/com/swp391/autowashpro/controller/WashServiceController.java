package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.WashServiceRequest;
import com.swp391.autowashpro.dto.WashServiceResponse;
import com.swp391.autowashpro.entity.WashService;
import com.swp391.autowashpro.service.WashServiceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/wash-services")
@CrossOrigin("*")
public class WashServiceController {
    private final WashServiceService washServiceService;

    public WashServiceController(WashServiceService washServiceService) {
        this.washServiceService = washServiceService;
    }

    @GetMapping
    @Operation(summary = "Get all wash services")
    public ResponseEntity<List<WashServiceResponse>> getAll(){
        return ResponseEntity.ok(washServiceService.getAllWashServices());
    }

    @PostMapping
    @Operation(summary = "Create a new wash service (Admin Only)")
    public ResponseEntity<?> create(@RequestBody WashServiceRequest request) {
        try {
            return ResponseEntity.ok(washServiceService.createService(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing wash service (Admin Only)")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody WashServiceRequest request) {
        try {
            return ResponseEntity.ok(washServiceService.updateService(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a wash service (Admin Only)")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            washServiceService.deleteService(id);
            return ResponseEntity.ok("Wash service deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
