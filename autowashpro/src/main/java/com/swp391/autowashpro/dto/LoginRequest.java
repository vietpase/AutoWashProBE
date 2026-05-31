package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Login key (username/phone) cannot be blank")
    private String loginKey;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
