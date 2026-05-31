package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.Vehicle;
import com.swp391.autowashpro.entity.WashService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WashServiceRepository extends JpaRepository<WashService,Integer>{
    boolean existsByServiceName(String serviceName);
}
