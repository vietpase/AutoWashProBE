package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Integer> {
    boolean existsByPromoName(String promoName);
    boolean existsByPromoNameAndPromoIdNot(String promoName, Integer promoId);
}

