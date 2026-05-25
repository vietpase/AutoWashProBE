package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount,Integer> {
    Optional<AdminAccount>findByUsername(String username);
}
