package com.swp391.autowashpro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWithVehicleRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;

    private String licensePlate;
    private String vehicleType;
    private String brand;
    private String color;
}
