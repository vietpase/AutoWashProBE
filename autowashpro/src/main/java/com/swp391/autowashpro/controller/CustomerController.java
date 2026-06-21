package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.CustomerResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
@Tag(name = "Customer Profile Management", description = "APIs for fetching and managing registered customer profile data")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Get customer profile details",
            description = "Fetches a full customer account snapshot including loyalty points, tier statuses, and linked personal credentials using the unique customer identity ID."
    )
    public ResponseEntity<?> getProfile(@RequestParam Integer customerId) {
        try {
            Customer customer = customerService.getCustomerProfile(customerId);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Get list of all customers",
            description = "Get list of all customers including fullName, phoneNumber, email, loyaltytier, currentPoint, totalVisits, totalSpend."
    )
    public ResponseEntity<?> getCustomerList(){
        try{
            List<CustomerResponse> customerList=customerService.getCustomerList();
            return ResponseEntity.ok().body(customerList);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}