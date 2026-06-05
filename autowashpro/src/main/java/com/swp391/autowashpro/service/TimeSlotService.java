package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.TimeSlotRequest;
import com.swp391.autowashpro.dto.TimeSlotResponse;
import com.swp391.autowashpro.entity.TimeSlot;
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

//   Create a new TimeSlot
    @Transactional
    public TimeSlotResponse createTimeSlot(TimeSlotRequest request){
//      validate duplicate slot name
        if (timeSlotRepository.existsBySlotName(request.getSlotName())) {
            throw new RuntimeException("Slot name '" + request.getSlotName() + "' already exists!");
        }
//      validate slot endTime and startTime
        if(request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().equals(request.getStartTime())){
            throw new RuntimeException("End time must be strictly after start time!");
        }

//      validate ovelapping timeslot
        if(timeSlotRepository.existsOverlappingSlot(request.getStartTime(),request.getEndTime())){
            throw new RuntimeException("The time period overlaps with an existing time slot in the database!");
        }
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlotName(request.getSlotName());
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setMaxCapacity(request.getMaxCapacity());
        timeSlot.setIsActive(request.getIsActive()!=null ? request.getIsActive() : true);
        TimeSlot savedSlot=timeSlotRepository.save(timeSlot);

        return new TimeSlotResponse(savedSlot);
    }

//  Update TimeSlot
    @Transactional
    public TimeSlotResponse updateTimeSlot(Integer id,TimeSlotRequest request){
        TimeSlot slot = timeSlotRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Time slot not found with ID: " + id));
//      validate slot endTime and startTime
        if(request.getEndTime().isBefore(request.getStartTime())|| request.getEndTime().equals(request.getStartTime())){
            throw new RuntimeException("End time must be strictly after start time!");
        }
//      validate slotName
        if (timeSlotRepository.existsBySlotNameAndSlotIdNot(request.getSlotName(), id)) {
            throw new RuntimeException("Slot name '" + request.getSlotName() + "' is already taken by another slot!");
        }
//      validate ovelapping timeslot except for this SlotID
        if (timeSlotRepository.existsOverlappingSlotExcludingId(request.getStartTime(), request.getEndTime(), id)) {
            throw new RuntimeException("The updated time period overlaps with another existing time slot!");
        }

        slot.setSlotName(request.getSlotName());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setMaxCapacity(request.getMaxCapacity());

        if(request.getIsActive()!=null){
            slot.setIsActive(request.getIsActive());
        }

        return new TimeSlotResponse(slot);
    }

    @Transactional
    public void deleteTimeSlot(Integer id){
        if (!timeSlotRepository.existsById(id)) {
            throw new RuntimeException("Time slot not found with ID: " + id);
        }
        try {
            timeSlotRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete this slot because it is already linked to existing customer bookings. Please set 'isActive' to false instead!");
        }
    }

}
