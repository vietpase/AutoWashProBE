package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.TimeSlotRequest;
import com.swp391.autowashpro.dto.TimeSlotResponse;
import com.swp391.autowashpro.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository){
        this.timeSlotRepository=timeSlotRepository;
    }


//  Get All TimeSlots
    public List<TimeSlotResponse> getAllTimeSlots(){
        return timeSlotRepository.findAll().stream().map(TimeSlotResponse::new).toList();
    }

////   Create a new TimeSlot
//    @Transactional
//    public TimeSlotResponse createTimeSlot(TimeSlotRequest request){
////      validate slot name
//        if (timeSlotRepository.existsBySlotName(request.getSlotName())) {
//            throw new RuntimeException("Slot name '" + request.getSlotName() + "' already exists!");
//        }
////      validate slot endTime and startTime
//        if(request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().equals(request.getStartTime())){
//            throw new RuntimeException("End time must be strictly after start time!");
//        }
//
//
//    }
}
