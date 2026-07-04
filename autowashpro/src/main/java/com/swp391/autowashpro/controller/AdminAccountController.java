package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.dto.StaffRequest;
import com.swp391.autowashpro.dto.StaffResponse;
import com.swp391.autowashpro.service.AdminAccountService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
@Tag(name = "Staff Account Management", description="This function is used by manager to manage staff accounts")
public class AdminAccountController {
    private final AdminAccountService adminAccountService;

    public AdminAccountController(AdminAccountService adminAccountService){
        this.adminAccountService=adminAccountService;
    }
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?>getAllStaffs(){
        try{
            return ResponseEntity.ok(adminAccountService.getAllStaffs());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createStaff(@RequestBody StaffRequest request){
        try{
            return ResponseEntity.ok(adminAccountService.createStaff(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateStaff(@PathVariable Integer id, @RequestBody StaffRequest request){
        try{
            return ResponseEntity.ok(adminAccountService.updateStaff(id, request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> deleteStaff(@PathVariable Integer id){
        try{
            adminAccountService.deleteStaff(id);
            return ResponseEntity.ok("Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
