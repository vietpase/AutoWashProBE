package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.LoyaltyTierRequest;
import com.swp391.autowashpro.dto.LoyaltyTierResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.entity.CustomerMonthlyStats;
import com.swp391.autowashpro.entity.LoyaltyTier;
import com.swp391.autowashpro.repository.CustomerMonthlyStatsRepository;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.repository.LoyaltyTierRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LoyaltyTierService {
    private final LoyaltyTierRepository loyaltyTierRepository;
    private final CustomerMonthlyStatsRepository customerMonthlyStatsRepository;
    private final CustomerRepository customerRepository;


    public LoyaltyTierService(LoyaltyTierRepository loyaltyTierRepository, CustomerMonthlyStatsRepository customerMonthlyStatsRepository,
                              CustomerRepository customerRepository){
        this.loyaltyTierRepository=loyaltyTierRepository;
        this.customerMonthlyStatsRepository = customerMonthlyStatsRepository;
        this.customerRepository = customerRepository;
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


//   Update loyaltyTier
    @Transactional
    public void reviewMonthlyCustomerTier(Customer customer, Date reviewDate) {
        // 1. Tự động tính ra chuỗi định dạng của tháng trước (Ví dụ: Tháng hiện tại là 2026-07 -> Tháng trước là "202606")
        YearMonth currentMonth = YearMonth.now();
        YearMonth previousMonth = currentMonth.minusMonths(1);
        String previousMonthStr = previousMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));

        // 2. Truy vấn dữ liệu chi tiêu của khách trong tháng trước
        CustomerMonthlyStats pastMonthStats = customerMonthlyStatsRepository
                .findByCustomerAndYearMonth(customer, previousMonthStr)
                .orElse(null);

        // Nếu tháng trước khách không hề tới tiệm rửa xe, mặc định chỉ số tháng đó bằng 0
        BigDecimal spendInMonth = BigDecimal.ZERO;
        int visitsInMonth = 0;

        if (pastMonthStats != null) {
            spendInMonth = pastMonthStats.getMonthlySpend() != null ? pastMonthStats.getMonthlySpend() : BigDecimal.ZERO;
            visitsInMonth = pastMonthStats.getMonthlyVisits() != null ? pastMonthStats.getMonthlyVisits() : 0;
        }

        // 3. Lấy tất cả các hạng từ DB lên, sắp xếp theo thứ tự ưu tiên TỪ CAO XUỐNG THẤP (DIAMOND -> BRONZE)
        List<LoyaltyTier> allTiers = loyaltyTierRepository.findAll();
        allTiers.sort(Comparator.comparing(LoyaltyTier::getPriorityLevel).reversed());

        // Mặc định ban đầu nếu không thỏa mãn mốc nào sẽ là hạng thấp nhất (BRONZE)
        LoyaltyTier appropriateTier = allTiers.get(allTiers.size() - 1);

        // 4. So sánh chỉ số của THÁNG TRƯỚC với điều kiện cấu hình của các hạng
        for (LoyaltyTier tier : allTiers) {
            if (spendInMonth.compareTo(tier.getMinSpending()) >= 0 && visitsInMonth >= tier.getMinVisits()) {
                appropriateTier = tier;
                break; // Tìm thấy hạng cao nhất thỏa mãn trong tháng trước, dừng vòng lặp
            }
        }

        // 5. Cập nhật hạng mới (Hệ thống tự hiểu là Giữ nguyên / Thăng hạng / Hạ hạng)
        if (!customer.getLoyaltyTier().getTierId().equals(appropriateTier.getTierId())) {
            log.info("Khách hàng ID {}: Thay đổi hạng từ {} -> {}",
                    customer.getCustomerId(), customer.getLoyaltyTier().getTierName(), appropriateTier.getTierName());
        }

        customer.setLoyaltyTier(appropriateTier);
        customer.setLastTierReview(reviewDate); // Ghi nhận ngày chạy quét thành công
        customerRepository.save(customer);
    }

}
