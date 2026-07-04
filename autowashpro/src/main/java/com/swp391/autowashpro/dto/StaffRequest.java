package com.swp391.autowashpro.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StaffRequest {
    @NotBlank(message = "Cannot be blank")
    private String fullName;
    @NotBlank(message = "Cannot be blank")
    private String username;
    @NotBlank(message = "Cannot be blank")
    private String password;
}
