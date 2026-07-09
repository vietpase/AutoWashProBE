package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.dto.MonthlyRevenue;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.entity.CustomerMonthlyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerMonthlyStatsRepository extends JpaRepository<CustomerMonthlyStats, Integer> {
    // Tìm kiếm thống kê của một khách hàng dựa vào ID và chuỗi định dạng YYYYMM
    Optional<CustomerMonthlyStats> findByCustomerCustomerIdAndYearMonth(Integer customerId, String yearMonth);

    Optional<CustomerMonthlyStats> findByCustomerAndYearMonth(Customer customer, String yearMonth);

    @Query("SELECT s.yearMonth AS yearMonth, COALESCE(SUM(s.monthlySpend), 0) AS totalRevenue " +
            "FROM CustomerMonthlyStats s " +
            "WHERE s.yearMonth >= :startMonth " +
            "GROUP BY s.yearMonth " +
            "ORDER BY s.yearMonth ASC")
    List<MonthlyRevenue> getRevenueLast6Months(@Param("startMonth") String startMonth);
}