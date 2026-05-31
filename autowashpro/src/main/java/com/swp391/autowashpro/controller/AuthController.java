package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }
//  Register Customer
    @PostMapping("/register")
    @Operation(summary = "Register new customer", description = "return ID,LoginKey(username/phoneNumber), fullName, roleName")
    public ResponseEntity<?>registerCustomer(@RequestBody RegisterRequest request){
        try {
        Customer customer = authService.registerCustomer(request);
             return ResponseEntity.ok("Register successfully for: "+customer.getFullName());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//  Login admin, customer
    @PostMapping("/login")
    @Operation(summary = "Login the system", description = "Return ID,LoginKey(username/phoneNumber), fullName,roleName")
    public ResponseEntity<?>login(@RequestBody LoginRequest request){
        try{
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return  ResponseEntity.status(401).body(e.getMessage());
        }
    }


//  Generate otp, send email
    @PostMapping("/forgot-password")
    @Operation(summary = "Send otp to reset password", description = "return otp in email registered")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        try{
            String message = authService.generateAndCreateOtp(request);
            return ResponseEntity.ok(message);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

//   Reset password
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password, save to db", description = "return a string successfully updated password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request){
        try{
            String message = authService.resetPassword(request);
            return ResponseEntity.ok(message);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
