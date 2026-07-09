package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.dto.LoyaltyTierCustomer;
import com.swp391.autowashpro.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findById(int customerId);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.totalVisits <= 1")
    long countNewCustomers();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.totalVisits > 1")
    long countOldCustomers();

    @Query("SELECT c.loyaltyTier.tierName AS tierName, COUNT(c) AS numberCustomer FROM Customer c GROUP BY c.loyaltyTier.tierName ")
    List<LoyaltyTierCustomer> countLoyaltyTierCustomer();


}
