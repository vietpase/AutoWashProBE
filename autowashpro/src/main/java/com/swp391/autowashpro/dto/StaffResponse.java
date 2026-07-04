package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.AdminAccount;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffResponse {
    private Integer id;
    private String fullName;
    private String userName;

    public StaffResponse(AdminAccount adminAccount){
        this.id = adminAccount.getAdminId();
        this.fullName=adminAccount.getFullName();
        this.userName = adminAccount.getUsername();
    }
}
