package com.swp391.autowashpro.dto;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
