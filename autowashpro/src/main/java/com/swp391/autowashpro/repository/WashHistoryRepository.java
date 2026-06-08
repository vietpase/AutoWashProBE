package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.WashHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WashHistoryRepository extends JpaRepository<WashHistory,Integer> {
}
