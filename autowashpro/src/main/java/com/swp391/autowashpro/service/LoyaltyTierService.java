package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.LoyaltyTierRequest;
import com.swp391.autowashpro.dto.LoyaltyTierResponse;
import com.swp391.autowashpro.entity.LoyaltyTier;
import com.swp391.autowashpro.repository.LoyaltyTierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyTierService {
    private final LoyaltyTierRepository loyaltyTierRepository;

    public LoyaltyTierService(LoyaltyTierRepository loyaltyTierRepository){
        this.loyaltyTierRepository=loyaltyTierRepository;
    }

//  Get All LoyaltyTiers
    public List<LoyaltyTierResponse> getAllLoyaltyTiers(){
        return loyaltyTierRepository.findAll().stream().map(LoyaltyTierResponse::new).toList();
    }

//  Create a new LoyaltyTier
    public LoyaltyTierResponse createLoyaltyTier(LoyaltyTierRequest request){
        if(loyaltyTierRepository.existsByTierName(request.getTierName())){
            throw new RuntimeException("This loyaltyTier is already created!");
        }
        LoyaltyTier loyaltyTier = new LoyaltyTier();
        loyaltyTier.setTierName(request.getTierName());
        loyaltyTier.setMinSpending(request.getMinSpending());
        loyaltyTier.setMinVisits(request.getMinVisits());
        loyaltyTier.setBookingWindowDays(request.getBookingWindowDays());
        loyaltyTier.setDiscountPercent(request.getDiscountPercent());
        loyaltyTier.setPointMultiplier(request.getPointMultiplier());
        loyaltyTier.setPriorityLevel(request.getPriorityLevel());
        loyaltyTier.setIsActive(request.getIsActive());

        LoyaltyTier savedLoyaltyTier = loyaltyTierRepository.save(loyaltyTier);

        return  new LoyaltyTierResponse(savedLoyaltyTier);
    }

//  Update a LoyaltyTier



}
