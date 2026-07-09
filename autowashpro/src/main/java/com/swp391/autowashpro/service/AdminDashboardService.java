package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.AdminDashboardResponse;
import com.swp391.autowashpro.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class AdminDashboardService {
    private final LoyaltyTierRepository loyaltyTierRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final PromotionRepository promotionRepository;
    private final RewardCatalogRepository rewardCatalogRepository;
    private final CustomerMonthlyStatsService customerMonthlyStatsService;

    public AdminDashboardService(LoyaltyTierRepository loyaltyTierRepository, CustomerRepository customerRepository,
                                 BookingRepository bookingRepository, PromotionRepository promotionRepository,
                                    RewardCatalogRepository rewardCatalogRepository, CustomerMonthlyStatsService customerMonthlyStatsService){
        this.loyaltyTierRepository = loyaltyTierRepository;
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.promotionRepository = promotionRepository;
        this.rewardCatalogRepository =rewardCatalogRepository;
        this.customerMonthlyStatsService =customerMonthlyStatsService;
    }


    public AdminDashboardResponse dashboardResponse(){
        AdminDashboardResponse adminDashboardResponse = new AdminDashboardResponse();
        LocalDate today = LocalDate.now();

        adminDashboardResponse.setTotalBooking(bookingRepository.countTotalBooking());
        adminDashboardResponse.setTodayBooking(bookingRepository.countByBookingDate(today));
        adminDashboardResponse.setTotalRevenue(bookingRepository.getTotalRevenue());
        adminDashboardResponse.setNewCustomers(customerRepository.countNewCustomers());
        adminDashboardResponse.setOldCustomers(customerRepository.countOldCustomers());
        adminDashboardResponse.setActivePromotion(promotionRepository.countByIsActiveTrue());
        adminDashboardResponse.setActiveReward(rewardCatalogRepository.countByIsActiveTrue());
        adminDashboardResponse.setServiceCustomers(bookingRepository.countServiceCustomer());
        adminDashboardResponse.setLoyaltyTierCustomers(customerRepository.countLoyaltyTierCustomer());
        adminDashboardResponse.setMonthlyRevenues(customerMonthlyStatsService.getSixMonthsRevenue());

    return  adminDashboardResponse;
    }




}
