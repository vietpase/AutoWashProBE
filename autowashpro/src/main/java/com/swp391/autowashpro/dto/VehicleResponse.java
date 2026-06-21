package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Vehicle;
import com.swp391.autowashpro.repository.VehicleRepository;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class VehicleResponse {
    private Integer vehicleId;
    private String licensePlate;
    private String vehicleType;
    private String brand;
    private String color;
    private Boolean isActive;

    public VehicleResponse(Vehicle vehicle){
        this.vehicleId=vehicle.getVehicleId();
        this.licensePlate = vehicle.getLicensePlate();
        this.vehicleType = vehicle.getVehicleType();
        this.brand = vehicle.getBrand();
        this.color = vehicle.getColor();
        this.isActive=vehicle.getIsActive();
    }

}
