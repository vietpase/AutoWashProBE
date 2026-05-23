package com.swp391.autowashpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private int id;
    private String loginKey;
    private String fullName;
    private String roleName;
}
