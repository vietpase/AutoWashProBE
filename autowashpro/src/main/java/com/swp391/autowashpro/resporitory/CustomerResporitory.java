package com.swp391.autowashpro.resporitory;

import com.swp391.autowashpro.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerResporitory extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByEmail(String email);
}
