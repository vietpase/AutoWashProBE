package com.swp391.autowashpro.resporitory;

import com.swp391.autowashpro.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleResporitory extends JpaRepository<Vehicle,Integer> {
}
