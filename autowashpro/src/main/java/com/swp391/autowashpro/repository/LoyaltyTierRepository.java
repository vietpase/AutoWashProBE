package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.LoyaltyTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyTierRepository extends JpaRepository <LoyaltyTier,Integer> {

}

