package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.MonthlyRevenue;
import com.swp391.autowashpro.repository.CustomerMonthlyStatsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import java.util.List;


@Service
public class CustomerMonthlyStatsService {
    private final CustomerMonthlyStatsRepository customerMonthlyStatsRepository;

    public CustomerMonthlyStatsService(CustomerMonthlyStatsRepository customerMonthlyStatsRepository){
        this.customerMonthlyStatsRepository=customerMonthlyStatsRepository;
    }

    @Transactional
    public List<MonthlyRevenue> getSixMonthsRevenue() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        //Lấy dữ liệu từ Repository
        String startMonthStr = startMonth.format(formatter);
        List<MonthlyRevenue> queryResult = customerMonthlyStatsRepository.getRevenueLast6Months(startMonthStr);

        return queryResult;
    }
}
