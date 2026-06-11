package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Tag(name = "Authentication Management", description = "APIs for user registration, multi-role authentication, and password recovery workflows")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new customer",
            description = "Handles public registration for new customers. Validates inputs and creates a default customer profile."
    )
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterRequest request){
        try {
            Customer customer = authService.registerCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Register successfully for: " + customer.getFullName());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "System login gateway",
            description = "Authenticates credentials for all roles (Customers, Staff, Managers). Returns access keys and user identity details upon success."
    )
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        try {
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Initiate password reset (Generate OTP)",
            description = "Validates the registered email address, triggers a secure OTP token, and dispatches it to the user's gmail."
    )
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        try {
            String message = authService.generateAndCreateOtp(request);
            return ResponseEntity.ok(message);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Finalize password reset",
            description = "Verifies the provided OTP token against the requested user account and overwrites the old password with the newly submitted one."
    )
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        try {
            String message = authService.resetPassword(request);
            return ResponseEntity.ok(message);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}