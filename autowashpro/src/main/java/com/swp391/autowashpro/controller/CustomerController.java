package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Get customer profile details
    @GetMapping("/profile")
    @Operation(summary = "Customer profile")
    public ResponseEntity<?> getProfile(@RequestParam int customerId) {
        try {
            Customer customer = customerService.getCustomerProfile(customerId);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}