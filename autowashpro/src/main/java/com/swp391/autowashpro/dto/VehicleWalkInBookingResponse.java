package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Vehicle;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleWalkInBookingResponse {
    private String licensePlate;
    private String brand;
    private String color;
    private String vehicleType;


    public VehicleWalkInBookingResponse(Vehicle vehicle) {
        if (vehicle != null) {
            this.licensePlate = vehicle.getLicensePlate();
            this.brand = vehicle.getBrand();
            this.color = vehicle.getColor();
            this.vehicleType = vehicle.getVehicleType();
        }
    }
}