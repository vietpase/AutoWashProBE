package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.RewardCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RewardCatalogRepository extends JpaRepository<RewardCatalog, Integer> {
    boolean existsByRewardName(String rewardName);
    boolean existsByRewardNameAndRewardIdNot(String rewardName, Integer rewardId);

    List<RewardCatalog> findByIsActiveTrueAndStockQuantityGreaterThanOrderByPointsRequiredAsc(Integer minStock);

    List<RewardCatalog> findAllByOrderByPointsRequiredAsc();
}