package com.swp391.autowashpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "CustomerMonthlyStats",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "year_month"})
            // Đảm bảo 1 khách hàng chỉ có 1 bản ghi duy nhất cho 1 tháng cụ thể
        }
    )
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CustomerMonthlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id")
    private int statsId;

    @Column(name = "year_month", columnDefinition = "CHAR(6)", nullable = false)
    private String yearMonth; //Format YYYYMM

    @Column(name = "monthly_spend", precision = 18,scale = 2)
    private BigDecimal monthlySpend = BigDecimal.ZERO;

    @Column(name = "monthly_visits")
    private Integer monthlyVisits = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Lấy tháng năm hiện tại và chuyển thành chuỗi "202605"
    String currentPeriod = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
}
