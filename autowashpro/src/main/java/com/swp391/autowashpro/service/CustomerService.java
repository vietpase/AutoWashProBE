package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.AuthResponse;
import com.swp391.autowashpro.dto.RegisterWithVehicleRequest;
import com.swp391.autowashpro.dto.VehicleRequest;
import com.swp391.autowashpro.dto.VehicleResponse;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.entity.Vehicle;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.repository.VehicleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public CustomerService(BCryptPasswordEncoder passwordEncoder, VehicleRepository vehicleRepository, CustomerRepository customerRepository){
        this.passwordEncoder = passwordEncoder;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }
//  New register with Vehicle(Vehicle + login)
    public AuthResponse registerNewCustomerWithVehicle (RegisterWithVehicleRequest request){
        var customerOpt = customerRepository.findByPhoneNumber(request.getPhoneNumber());
        if(customerOpt.isPresent()){
            throw new RuntimeException("This phone number is already registered!");
        }

        Customer customer = new Customer();
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        Customer savedCustomer = customerRepository.save(customer);

        if(request.getLicensePlate()!=null &&!request.getLicensePlate().isEmpty()){
            Vehicle vehicle = new Vehicle();
            vehicle.setLicensePlate(request.getLicensePlate());
            vehicle.setVehicleType(request.getVehicleType());
            vehicle.setBrand(request.getBrand());
            vehicle.setColor(request.getColor());
            vehicle.setCustomer(savedCustomer);

            vehicleRepository.save(vehicle);
        }

        return new AuthResponse(savedCustomer.getCustomerId(), savedCustomer.getPhoneNumber(),
                savedCustomer.getFullName(), "ROLE_CUSTOMER");
    }


//  View Customer profile
    public Customer getCustomerProfile(int customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(()-> new RuntimeException("Customer not found with ID: " + customerId));
    }

//  View Vehicle List
    public List<VehicleResponse> getMyVehicles(int customerId){
        List<Vehicle> vehicles =vehicleRepository.findByCustomer_CustomerId(customerId);
        return vehicles.stream().map(VehicleResponse::new).toList();
    }

//  Add new vehicle
    public VehicleResponse addVehicle (VehicleRequest request){
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(()->new RuntimeException("Customer not found to add vehicle!"));

        Vehicle vehicle = new Vehicle();
        vehicle.setCustomer(customer);
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setColor(request.getColor());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setVehicleId(savedVehicle.getVehicleId());
        vehicleResponse.setLicensePlate(savedVehicle.getLicensePlate());
        vehicleResponse.setVehicleType(savedVehicle.getVehicleType());
        vehicleResponse.setBrand(savedVehicle.getBrand());
        vehicleResponse.setColor(savedVehicle.getColor());

        return vehicleResponse;
    }

//  Delete vehicle
    public void deleteVehicle(int vehicleId){
        if(!vehicleRepository.existsById(vehicleId)){
            throw new RuntimeException("Vehicle ID does not exist in system!");
        }
        vehicleRepository.deleteById(vehicleId);
    }


}
