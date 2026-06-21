package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.CustomerResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Giờ chỉ phụ thuộc vào những gì liên quan đến Customer
    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // View Customer profile
    public Customer getCustomerProfile(int customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }
    // Get list of all customer
    public List<CustomerResponse> getCustomerList(){
        return customerRepository.findAll().stream().map(CustomerResponse::new).toList();
    }
}