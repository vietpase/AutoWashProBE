package com.swp391.autowashpro.scheduler;

import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.service.LoyaltyTierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyScheduler {

    private final CustomerRepository customerRepository;
    private final LoyaltyTierService loyaltyTierService;

    /**
     * Chạy định kỳ vào lúc 00:00:00 ngày ngày đầu tiên của mỗi tháng
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    public void performMonthlyTierReview() {
        log.info("=== [CRON JOB] BẮT ĐẦU XÉT DUYỆT LẠI HẠNG THÀNH VIÊN ĐẦU THÁNG ===");

        List<Customer> allCustomers = customerRepository.findAll();
        Date currentReviewDate = new Date();

        for (Customer customer : allCustomers) {
            try {
                loyaltyTierService.reviewMonthlyCustomerTier(customer, currentReviewDate);
            } catch (Exception e) {
                log.error("Lỗi khi quét review hạng cho khách ID {}: {}", customer.getCustomerId(), e.getMessage());
            }
        }

        log.info("=== [CRON JOB] HOÀN THÀNH TIẾN TRÌNH XÉT DUYỆT HẠNG ===");
    }
}