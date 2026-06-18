package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.RewardRedemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RewardRedemptionRepository extends JpaRepository<RewardRedemption, Integer> {

    List<RewardRedemption> findByCustomerCustomerIdOrderByRedemptionDateDesc(Integer customerId);

    // Lấy danh sách voucher hợp lệ của khách hàng chưa bị áp vào Booking nào
    List<RewardRedemption> findByCustomerCustomerIdAndBookingIsNullOrderByRedemptionDateDesc(Integer customerId);
}