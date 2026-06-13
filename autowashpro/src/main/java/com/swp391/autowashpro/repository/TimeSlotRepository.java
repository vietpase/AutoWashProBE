package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    Boolean existsBySlotName(String slotName);
    Boolean existsBySlotNameAndSlotIdNot(String slotName, Integer slotId);
    List<TimeSlot> findByIsActiveTrue();

    // Sử dụng CAST AS time để ép kiểu tham số truyền vào
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TimeSlot t " +
            "WHERE t.startTime < CAST(:endTime AS time) AND t.endTime > CAST(:startTime AS time)")
    boolean existsOverlappingSlot(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    // Sử dụng CAST AS time để ép kiểu tham số truyền vào
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TimeSlot t " +
            "WHERE t.slotId != :slotId AND t.startTime < CAST(:endTime AS time) AND t.endTime > CAST(:startTime AS time)")
    boolean existsOverlappingSlotExcludingId(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, @Param("slotId") Integer slotId);
}