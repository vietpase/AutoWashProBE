package com.swp391.autowashpro.controller;

import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.service.LoyaltyTierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final CustomerRepository customerRepository;
    private final LoyaltyTierService loyaltyTierService;

    /**
     * API dùng để Demo/Test kích hoạt quét hạng bằng tay
     * Đường dẫn: GET http://localhost:8080/api/test/force-review
     */
    @GetMapping("/force-review")
    public ResponseEntity<String> forceMonthlyReview() {
        List<Customer> allCustomers = customerRepository.findAll();
        Date currentReviewDate = new Date();

        for (Customer customer : allCustomers) {
            // Ép hệ thống chạy hàm xét duyệt ngay lập tức
            loyaltyTierService.reviewMonthlyCustomerTier(customer, currentReviewDate);
        }

        return ResponseEntity.ok("Force run thành công! Đã cập nhật lại hạng cho toàn bộ khách hàng.");
    }
}