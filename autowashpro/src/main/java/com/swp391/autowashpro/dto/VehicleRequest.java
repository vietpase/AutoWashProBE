package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequest {
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    @NotBlank(message = "License plate cannot be blank")
    private String licensePlate;
    @NotBlank(message = "Vehicle type cannot be blank")
    private String vehicleType;
    private String brand;
    private String color;
    private Boolean isActive;
}
