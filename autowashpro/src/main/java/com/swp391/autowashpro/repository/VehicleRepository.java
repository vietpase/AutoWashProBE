package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {
    List<Vehicle> findByCustomer_CustomerId(int customerId);
}
