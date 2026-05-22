package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.AuthResponse;
import com.swp391.autowashpro.dto.LoginRequest;
import com.swp391.autowashpro.dto.RegisterRequest;
import com.swp391.autowashpro.entity.Booking;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.resporitory.AdminAccountResporitory;
import com.swp391.autowashpro.resporitory.CustomerResporitory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CustomerResporitory customerResporitory;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminAccountResporitory adminAccountResporitory;

    public AuthService(CustomerResporitory customerResporitory, BCryptPasswordEncoder passwordEncoder, AdminAccountResporitory adminAccountResporitory){
        this.customerResporitory = customerResporitory;
        this.passwordEncoder = passwordEncoder;
        this.adminAccountResporitory= adminAccountResporitory;
    }
//    Register Customer
    public Customer registerCustomer(RegisterRequest request){
        if(customerResporitory.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new RuntimeException("This phone number is already logged in the system!");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return customerResporitory.save(customer);
    }

//    public AuthResponse login(LoginRequest request){
//        var adminOpt = adminAccountResporitory.findByUsername(request.getLoginKey());
//
//    }

//


}
