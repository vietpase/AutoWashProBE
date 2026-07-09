package com.swp391.autowashpro.repository;

import com.swp391.autowashpro.entity.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount,Integer> {
    Optional<AdminAccount>findByAdminId(Integer id);
    Optional<AdminAccount>findByUsername(String username);
    Boolean existsByUsername(String userName);
    Boolean existsByUsernameAndAdminIdNot(String username, Integer adminId);

    @Query("SELECT a FROM AdminAccount a WHERE a.role NOT LIKE '%MANAGER%'")
    List<AdminAccount> findAllStaff();


}
