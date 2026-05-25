package com.swp391.autowashpro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequest {
    private int customerId;
    private String licensePlate;
    private String vehicleType;
    private String brand;
    private String color;
}
