package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotREpository extends JpaRepository<TimeSlot,Integer> {
}
