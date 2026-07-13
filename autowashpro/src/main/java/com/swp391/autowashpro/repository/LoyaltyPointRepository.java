package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.dto.PointTransactionResponse;
import com.swp391.autowashpro.entity.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint,Integer> {
    List<LoyaltyPoint> findByCustomerCustomerIdOrderByCreatedAtDesc(Integer customerId);
}
