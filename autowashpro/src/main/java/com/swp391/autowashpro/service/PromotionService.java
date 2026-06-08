package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.PromotionRequest;
import com.swp391.autowashpro.dto.PromotionResponse;
import com.swp391.autowashpro.entity.LoyaltyTier;
import com.swp391.autowashpro.entity.Promotion;
import com.swp391.autowashpro.repository.LoyaltyTierRepository;
import com.swp391.autowashpro.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final LoyaltyTierRepository loyaltyTierRepository;

    public PromotionService(PromotionRepository promotionRepository, LoyaltyTierRepository loyaltyTierRepository){
        this.promotionRepository = promotionRepository;
        this.loyaltyTierRepository = loyaltyTierRepository;
    }

    // Get all promotions (Admin/Manager view - sees everything)
    public List<PromotionResponse> getAllPromotions(){
        return promotionRepository.findAll().stream().map(PromotionResponse::new).toList();
    }

    // Get only active promotions (Customer view)
    public List<PromotionResponse> getActivePromotions() {
        return promotionRepository.findByIsActiveTrue().stream().map(PromotionResponse::new).toList();
    }

    // Create a new promotion
    @Transactional
    public PromotionResponse createPromotion(PromotionRequest request){
        if(promotionRepository.existsByPromoName(request.getPromoName())){
            throw new RuntimeException("Promotion with name '" + request.getPromoName() + "' already exists!");
        }

        if(request.getEndDate().isBefore(request.getStartDate())){
            throw new RuntimeException("End date cannot be before start date!");
        }

        Promotion promotion = new Promotion();
        promotion.setPromoName(request.getPromoName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountAmount(request.getDiscountAmount());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());

        // Thay đổi mapping status cũ sang isActive
        if (request.getIsActive() != null) {
            promotion.setIsActive(request.getIsActive());
        } else {
            promotion.setIsActive(true);
        }

        if (request.getMinTierId() != null) {
            LoyaltyTier tier = loyaltyTierRepository.findById(request.getMinTierId())
                    .orElseThrow(() -> new RuntimeException("Loyalty tier not found with ID: " + request.getMinTierId()));
            promotion.setLoyaltyTier(tier);
        }

        return new PromotionResponse(promotionRepository.save(promotion));
    }

    // Update promotion
    @Transactional
    public PromotionResponse updatePromotion(Integer id, PromotionRequest request) {
        Promotion promo = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));

        if (promotionRepository.existsByPromoNameAndPromoIdNot(request.getPromoName(), id)) {
            throw new RuntimeException("Promo name '" + request.getPromoName() + "' is already taken by another promotion!");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date!");
        }

        promo.setPromoName(request.getPromoName());
        promo.setDescription(request.getDescription());
        promo.setDiscountAmount(request.getDiscountAmount());
        promo.setStartDate(request.getStartDate());
        promo.setEndDate(request.getEndDate());

        // Cập nhật trạng thái isActive mới
        if (request.getIsActive() != null) {
            promo.setIsActive(request.getIsActive());
        }

        if (request.getMinTierId() != null) {
            LoyaltyTier tier = loyaltyTierRepository.findById(request.getMinTierId())
                    .orElseThrow(() -> new RuntimeException("Loyalty tier not found with ID: " + request.getMinTierId()));
            promo.setLoyaltyTier(tier);
        } else {
            promo.setLoyaltyTier(null);
        }

        return new PromotionResponse(promotionRepository.save(promo));
    }

    @Transactional
    public void deletePromotion(Integer promoId) {
        Promotion promo = promotionRepository.findById(promoId)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + promoId));

        promo.setIsActive(false);
        promotionRepository.save(promo);
    }
}