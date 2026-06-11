package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.LoyaltyTierRequest;
import com.swp391.autowashpro.dto.LoyaltyTierResponse;
import com.swp391.autowashpro.entity.LoyaltyTier;
import com.swp391.autowashpro.repository.LoyaltyTierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyTierService {
    private final LoyaltyTierRepository loyaltyTierRepository;

    public LoyaltyTierService(LoyaltyTierRepository loyaltyTierRepository){
        this.loyaltyTierRepository=loyaltyTierRepository;
    }

//  Get All LoyaltyTiers for manager
        @Transactional
        public List<LoyaltyTierResponse> getAllTiers() {

            List<LoyaltyTier> tiers =
                    loyaltyTierRepository.findAllByOrderByPriorityLevelAsc();

            System.out.println("Found: " + tiers.size());

            return tiers.stream()
                    .map(LoyaltyTierResponse::new)
                    .toList();
        }
    //  Get All LoyaltyTiers for customer
    @Transactional
    public List<LoyaltyTierResponse> getActiveTiers() {
        // Chỉ trả về các hạng đang hoạt động (dùng cho client hiển thị công khai)
        return loyaltyTierRepository.findByIsActiveTrueOrderByPriorityLevelAsc().stream()
                .map(LoyaltyTierResponse::new)
                .toList();
    }

//  Create a new LoyaltyTier
    @Transactional
    public LoyaltyTierResponse createTier(LoyaltyTierRequest request) {
        if (loyaltyTierRepository.existsByTierName(request.getTierName())) {
            throw new RuntimeException("Loyalty tier name '" + request.getTierName() + "' already exists!");
        }

        LoyaltyTier tier = new LoyaltyTier();
        tier.setTierName(request.getTierName());
        tier.setMinSpending(request.getMinSpending());
        tier.setMinVisits(request.getMinVisits());
        tier.setBookingWindowDays(request.getBookingWindowDays());
        tier.setPointMultiplier(request.getPointMultiplier());
        tier.setPriorityLevel(request.getPriorityLevel());
        tier.setDiscountPercent(request.getDiscountPercent());
        tier.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return new LoyaltyTierResponse(loyaltyTierRepository.save(tier));
    }

//  Update a LoyaltyTier
    @Transactional
    public LoyaltyTierResponse updateTier(Integer id, LoyaltyTierRequest request) {
        LoyaltyTier tier = loyaltyTierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loyalty tier not found with ID: " + id));

        if (loyaltyTierRepository.existsByTierNameAndTierIdNot(request.getTierName(), id)) {
            throw new RuntimeException("Loyalty tier name '" + request.getTierName() + "' is already taken!");
        }

        tier.setTierName(request.getTierName());
        tier.setMinSpending(request.getMinSpending());
        tier.setMinVisits(request.getMinVisits());
        tier.setBookingWindowDays(request.getBookingWindowDays());
        tier.setPointMultiplier(request.getPointMultiplier());
        tier.setPriorityLevel(request.getPriorityLevel());
        tier.setDiscountPercent(request.getDiscountPercent());
        if (request.getIsActive() != null) {
            tier.setIsActive(request.getIsActive());
        }

        return new LoyaltyTierResponse(loyaltyTierRepository.save(tier));
    }

//  Delete
    @Transactional
    public void deleteTier(Integer id) {
        LoyaltyTier tier = loyaltyTierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loyalty tier not found with ID: " + id));

        // Thực hiện Soft Delete (Xóa mềm) để bảo vệ các Customer đang liên kết với Tier này
        tier.setIsActive(false);
        loyaltyTierRepository.save(tier);
    }

}
