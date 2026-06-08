package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.CustomerMonthlyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerMonthlyStatsRepository extends JpaRepository<CustomerMonthlyStats, Integer> {
    // Tìm kiếm thống kê của một khách hàng dựa vào ID và chuỗi định dạng YYYYMM
    Optional<CustomerMonthlyStats> findByCustomerCustomerIdAndYearMonth(Integer customerId, String yearMonth);
}