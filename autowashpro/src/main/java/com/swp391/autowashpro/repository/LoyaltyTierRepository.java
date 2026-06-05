package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.dto.LoyaltyTierRequest;
import com.swp391.autowashpro.entity.LoyaltyTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyTierRepository extends JpaRepository <LoyaltyTier,Integer> {
    Boolean existsByTierName(String tierName);
    Boolean existsByTierNameAndTierIdNot(String tierName,Integer tierId);
    // Tìm các hạng đang hoạt động xếp theo cấp độ ưu tiên tăng dần (dành cho phía Customer xem)
    List<LoyaltyTier> findByIsActiveTrueOrderByPriorityLevelAsc();
    // Tìm tất cả các hạng xếp theo cấp độ ưu tiên (dành cho Manager quản trị)
    List<LoyaltyTier> findAllByOrderByPriorityLevelAsc();
}

