package com.swp391.autowashpro.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginKey;
    private String password;
}
