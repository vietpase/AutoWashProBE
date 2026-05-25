package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.AuthResponse;
import com.swp391.autowashpro.dto.RegisterWithVehicleRequest;
import com.swp391.autowashpro.dto.VehicleRequest;
import com.swp391.autowashpro.dto.VehicleResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/register-with-vehicle")
    public ResponseEntity<?>registerWithVehicle(@RequestBody RegisterWithVehicleRequest request){
        try{
            AuthResponse response = customerService.registerNewCustomerWithVehicle(request);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam int customerId){
        try{
            Customer customer = customerService.getCustomerProfile(customerId);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//  Get a list of customer vehicles.
    @GetMapping("/vehicles")
    public ResponseEntity<?> getMyVehicle(@RequestParam int customerId){
        try{
            List<VehicleResponse> vehicles = customerService.getMyVehicles(customerId);
            return ResponseEntity.ok(vehicles);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//  Add a new Vehicle
    @PostMapping("/vehicles")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleRequest request){
        try{
            VehicleResponse savedVehicle = customerService.addVehicle(request);
            return ResponseEntity.ok(savedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//  Delete Vehicle by ID
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(@RequestBody int vehicleId){
        try {
            customerService.deleteVehicle(vehicleId);
            return ResponseEntity.ok("Vehicle deleted successfully with ID: " + vehicleId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
