package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerWalkInBookingResponse {
    private String email;
    private String phoneNumber;
    private String fullName;

    public CustomerWalkInBookingResponse(Customer customer){
        this.email = customer.getEmail();
        this.phoneNumber= customer.getPhoneNumber();
        this.fullName= customer.getFullName();
    }
}
