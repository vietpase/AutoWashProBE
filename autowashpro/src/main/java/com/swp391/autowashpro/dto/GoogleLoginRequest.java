package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {
    @NotBlank(message = "Google ID Token cannot be blank")
    private String token;
}