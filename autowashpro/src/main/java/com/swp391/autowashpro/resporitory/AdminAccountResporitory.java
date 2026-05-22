package com.swp391.autowashpro.resporitory;

import com.swp391.autowashpro.entity.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminAccountResporitory extends JpaRepository<AdminAccount,Integer> {
    Optional<AdminAccount>findByUsername(String username);
}
