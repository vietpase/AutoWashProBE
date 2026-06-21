package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkInBookingRequest {
    @NotBlank(message = "Customer name is required")
    private String walkInCustomerName;

    @NotBlank(message = "Phone number is required")
    private String walkInPhoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    private String vehicleType;
    private String brand;
    private String color;

    @NotNull(message = "Time Slot ID is required")
    private Integer slotId;

    @NotNull(message = "Wash Service ID is required")
    private Integer washServiceId;
}