package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.RedeemRequest;
import com.swp391.autowashpro.dto.RewardCatalogRequest;
import com.swp391.autowashpro.dto.RewardCatalogResponse;
import com.swp391.autowashpro.dto.RewardRedemptionResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.entity.RewardCatalog;
import com.swp391.autowashpro.entity.RewardRedemption;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.repository.RewardCatalogRepository;
import com.swp391.autowashpro.repository.RewardRedemptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardService {

    private final RewardCatalogRepository rewardCatalogRepository;
    private final RewardRedemptionRepository rewardRedemptionRepository;
    private final CustomerRepository customerRepository;

    public RewardService(RewardCatalogRepository rewardCatalogRepository,
                         RewardRedemptionRepository rewardRedemptionRepository,
                         CustomerRepository customerRepository) {
        this.rewardCatalogRepository = rewardCatalogRepository;
        this.rewardRedemptionRepository = rewardRedemptionRepository;
        this.customerRepository = customerRepository;
    }

    // ==========================================
    //  QUẢN LÝ DANH MỤC QUÀ (MANAGEMENT LOGICS)
    // ==========================================

    @Transactional(readOnly = true)
    public List<RewardCatalogResponse> getAllRewardsForAdmin() {
        return rewardCatalogRepository.findAllByOrderByPointsRequiredAsc().stream()
                .map(RewardCatalogResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RewardCatalogResponse> getActiveRewardsForCustomer() {
        return rewardCatalogRepository.findByIsActiveTrueAndStockQuantityGreaterThanOrderByPointsRequiredAsc(0).stream()
                .map(RewardCatalogResponse::new)
                .toList();
    }

    @Transactional
    public RewardCatalogResponse createReward(RewardCatalogRequest request) {
        if (rewardCatalogRepository.existsByRewardName(request.getRewardName())) {
            throw new RuntimeException("Reward name already exists!");
        }
        RewardCatalog reward = new RewardCatalog();
        reward.setRewardName(request.getRewardName());
        reward.setDescription(request.getDescription());
        reward.setPointsRequired(request.getPointsRequired());
        reward.setDiscountAmount(request.getDiscountAmount());
        reward.setStockQuantity(request.getStockQuantity());
        reward.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return new RewardCatalogResponse(rewardCatalogRepository.save(reward));
    }

    @Transactional
    public RewardCatalogResponse updateReward(Integer id, RewardCatalogRequest request) {
        RewardCatalog reward = rewardCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with ID: " + id));

        if (rewardCatalogRepository.existsByRewardNameAndRewardIdNot(request.getRewardName(), id)) {
            throw new RuntimeException("Reward name is already taken!");
        }

        reward.setRewardName(request.getRewardName());
        reward.setDescription(request.getDescription());
        reward.setPointsRequired(request.getPointsRequired());
        reward.setDiscountAmount(request.getDiscountAmount());
        reward.setStockQuantity(request.getStockQuantity());
        if (request.getIsActive() != null) {
            reward.setIsActive(request.getIsActive());
        }

        return new RewardCatalogResponse(rewardCatalogRepository.save(reward));
    }

    @Transactional
    public void deleteRewardSoft(Integer id) {
        RewardCatalog reward = rewardCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
        reward.setIsActive(false);
        rewardCatalogRepository.save(reward);
    }

    // ==========================================
    //   ĐỔI QUÀ & LỊCH SỬ ĐỔI (REDEMPTION LOGICS)
    // ==========================================

    @Transactional
    public RewardRedemptionResponse redeemRewardPoints(RedeemRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        RewardCatalog reward = rewardCatalogRepository.findById(request.getRewardId())
                .orElseThrow(() -> new RuntimeException("Reward item not found"));

        if (!reward.getIsActive()) {
            throw new RuntimeException("This reward is currently disabled.");
        }

        if (reward.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Out of stock! Only " + reward.getStockQuantity() + " items left.");
        }

        int totalPointsNeeded = reward.getPointsRequired() * request.getQuantity();

        if (customer.getCurrentPoints() < totalPointsNeeded) {
            throw new RuntimeException("Not enough loyalty points. You need " + totalPointsNeeded + " points.");
        }

        // Khấu trừ điểm của khách và tồn kho quà
        customer.setCurrentPoints(customer.getCurrentPoints()- totalPointsNeeded);
        reward.setStockQuantity(reward.getStockQuantity() - request.getQuantity());

        customerRepository.save(customer);
        rewardCatalogRepository.save(reward);

        // Ghi nhận vào lịch sử đổi quà
        RewardRedemption redemption = new RewardRedemption();
        redemption.setCustomer(customer);
        redemption.setRewardCatalog(reward);
        redemption.setPointsUsed(totalPointsNeeded);
        redemption.setRedemptionDate(LocalDateTime.now());
        redemption.setBooking(null); // Mặc định chưa áp dụng vào booking nào

        // KHÓA CỨNG SNAPSHOT THÔNG TIN TẠI THỜI ĐIỂM ĐỔI QUÀ
        redemption.setRewardNameAtRedemption(reward.getRewardName());
        redemption.setDiscountAmountAtRedemption(reward.getDiscountAmount());

        return new RewardRedemptionResponse(rewardRedemptionRepository.save(redemption));
    }

    @Transactional(readOnly = true)
    public List<RewardRedemptionResponse> getCustomerRedemptionHistory(Integer customerId) {
        return rewardRedemptionRepository.findByCustomerCustomerIdOrderByRedemptionDateDesc(customerId).stream()
                .map(RewardRedemptionResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RewardRedemptionResponse> getAvailableVouchersForCustomer(Integer customerId) {
        return rewardRedemptionRepository.findByCustomerCustomerIdAndBookingIsNullOrderByRedemptionDateDesc(customerId).stream()
                .map(RewardRedemptionResponse::new)
                .toList();
    }
}