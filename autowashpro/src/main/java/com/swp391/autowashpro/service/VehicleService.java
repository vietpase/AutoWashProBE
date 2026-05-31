package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.VehicleRequest;
import com.swp391.autowashpro.dto.VehicleResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.entity.Vehicle;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public VehicleService(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    // View Vehicle List
    public List<VehicleResponse> getMyVehicles(int customerId) {
        // Kiểm tra xem khách hàng có tồn tại không trước khi lấy danh sách xe
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        List<Vehicle> vehicles = vehicleRepository.findByCustomer_CustomerId(customerId);
        return vehicles.stream().map(VehicleResponse::new).toList();
    }

    // Add new vehicle
    @Transactional
    public VehicleResponse addVehicle(VehicleRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found to add vehicle!"));

        Optional<Vehicle> existingVehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate());
        if (existingVehicle.isPresent()) {
            throw new RuntimeException("This license plate has already been registered to another vehicle in the system!");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setCustomer(customer);
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setColor(request.getColor());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(savedVehicle);
    }

    // Delete vehicle
    @Transactional
    public void deleteVehicle(int vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new RuntimeException("Vehicle ID does not exist in system!");
        }
        vehicleRepository.deleteById(vehicleId);
    }

    // Update Vehicle
    @Transactional
    public VehicleResponse updateVehicle(Integer vehicleId, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        // Dùng .equalsIgnoreCase() để check không phân biệt chữ hoa chữ thường cho biển số
        if (!request.getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
            Optional<Vehicle> existingVehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate());
            if (existingVehicle.isPresent()) {
                throw new RuntimeException("This license plate has already been registered to another vehicle in the system!");
            }
            vehicle.setLicensePlate(request.getLicensePlate());
        }
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setColor(request.getColor());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(updatedVehicle);
    }
}