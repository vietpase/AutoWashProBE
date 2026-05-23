package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.service.AuthService;
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
//Register Customer
    @PostMapping("/register")
    public ResponseEntity<?>registerCustomer(@RequestBody RegisterRequest request){
        try {
        Customer customer = authService.registerCustomer(request);
             return ResponseEntity.ok("Register successfully for: "+customer.getFullName());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//  login admin, customer
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginRequest request){
        try{
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return  ResponseEntity.status(401).body(e.getMessage());
        }
    }


//    generate otp, send email
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        try{
            String message = authService.generateAndCreateOtp(request);
            return ResponseEntity.ok(message);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

//    reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request){
        try{
            String message = authService.resetPassword(request);
            return ResponseEntity.ok(message);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
