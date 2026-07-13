package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.PointTransactionResponse;
import com.swp391.autowashpro.repository.LoyaltyPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyPointService {
    private final LoyaltyPointRepository loyaltyPointRepository;

    public LoyaltyPointService(LoyaltyPointRepository loyaltyPointRepository){
        this.loyaltyPointRepository=loyaltyPointRepository;
    }

    @Transactional
    public List<PointTransactionResponse> getPointTransaction(Integer customerId){
        return loyaltyPointRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(PointTransactionResponse::new).toList();
    }
}
