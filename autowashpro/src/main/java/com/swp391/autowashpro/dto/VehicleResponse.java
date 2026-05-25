package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Vehicle;
import com.swp391.autowashpro.repository.VehicleRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleResponse {
    private int vehicleId;
    private String licensePlate;
    private String vehicleType;
    private String brand;
    private String color;
    private int customerId;

    public VehicleResponse(Vehicle vehicle){
        this.vehicleId=vehicle.getVehicleId();
        this.licensePlate = vehicle.getLicensePlate();
        this.vehicleType = vehicle.getVehicleType();
        this.brand = vehicle.getBrand();
        this.color = vehicle.getColor();
        this.customerId = vehicle.getCustomer().getCustomerId();
    }

}
