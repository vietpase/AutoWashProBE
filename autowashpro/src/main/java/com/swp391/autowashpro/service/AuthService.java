package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.resporitory.AdminAccountResporitory;
import com.swp391.autowashpro.resporitory.CustomerResporitory;
import com.swp391.autowashpro.resporitory.LoyaltyTierResporitory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final CustomerResporitory customerResporitory;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminAccountResporitory adminAccountResporitory;
    private final LoyaltyTierResporitory loyaltyTierResporitory;
    private JavaMailSender javaMailSender;

    //Record OTP(Key: Email, Value: OTP)
    private final Map<String,String>otpStorage=new HashMap<>();

    public AuthService(CustomerResporitory customerResporitory, BCryptPasswordEncoder passwordEncoder,
                       AdminAccountResporitory adminAccountResporitory, LoyaltyTierResporitory loyaltyTierResporitory,
                       JavaMailSender javaMailSender){
        this.customerResporitory = customerResporitory;
        this.passwordEncoder = passwordEncoder;
        this.adminAccountResporitory= adminAccountResporitory;
        this.loyaltyTierResporitory = loyaltyTierResporitory;
        this.javaMailSender=javaMailSender;

    }

//    Register Customer
    public Customer registerCustomer(RegisterRequest request){
        if(customerResporitory.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new RuntimeException("This phone number is already logged in the system!");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return customerResporitory.save(customer);
    }

//Login
    public AuthResponse login(LoginRequest request){
//        TH1: Check admin
        var adminOpt = adminAccountResporitory.findByUsername((request.getLoginKey()));
        if(adminOpt.isPresent()){
            var admin =adminOpt.get();
            if(!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
                throw new RuntimeException("The administrator account password is incorrect!");
            }
            String roleName = "ROLE_"+ admin.getRole().toUpperCase();
            return new AuthResponse(admin.getAdminId(),
                    admin.getUsername(),admin.getFullName(),roleName);
        }

//        TH2: Check customer
        var customerOpt = customerResporitory.findByPhoneNumber(request.getLoginKey());
        if(customerOpt.isPresent()){
            var customer = customerOpt.get();
            if(!passwordEncoder.matches(request.getPassword(), customer.getPassword())){
                throw new RuntimeException("The customer account password is incorrect!");
            }
            String roleName = "ROLE_CUSTOMER";
            return new AuthResponse(customer.getCustomerId(),customer.getPhoneNumber(),
                                    customer.getFullName(),roleName);
        }

//        TH3: No userName, phoneNumber matched
        throw new RuntimeException("The account or phone number does not exist in the system!");
    }


//Create and Send OTP through Email
    public String generateAndCreateOtp(ForgotPasswordRequest request){
        Optional<Customer> customerOpt = customerResporitory.findByEmail(request.getEmail());
        if(customerOpt.isEmpty()){
            throw new RuntimeException("The email address does not exist in the system!");
        }
        String otp = String.format("%06d",new Random().nextInt(999999));
        otpStorage.put(request.getEmail(),otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("OTP Password Recovery Code - AutoWashPro");
        message.setText("Hello"+customerOpt.get().getFullName()+"\n\n"+
                "The OTP code to recover your password is:"+otp+"\n"+
                "Please do not share this code with anyone.\n\n"+
                "Sincerely, The AutoWashPro Team."
        );

        javaMailSender.send(message);
        return"The OTP code has been sent to your email!";
    }

//    Confirm OTP and change your password.
    public String resetPassword(ResetPasswordRequest request){
        String storedOtp = otpStorage.get(request.getEmail());
        if(storedOtp == null || !storedOtp.equals(request.getOtp())){
            throw new RuntimeException("The OTP code is incorrect or has expired!");
        }
        Customer customer = customerResporitory.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Error: User not found!"));

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerResporitory.save(customer);
        otpStorage.remove(request.getEmail());
        return"Password changed successfully! You can now log in with your new password.";
    }




}
