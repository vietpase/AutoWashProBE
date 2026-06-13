package com.swp391.autowashpro.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.entity.Customer;
import com.swp391.autowashpro.repository.AdminAccountRepository;
import com.swp391.autowashpro.repository.CustomerRepository;
import com.swp391.autowashpro.repository.LoyaltyTierRepository;
import com.swp391.autowashpro.security.JwtService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {
    private static final String GOOGLE_CLIENT_ID = "214094040607-31ti9r8hjgq27nu9jv1maqgc2stg50u3.apps.googleusercontent.com";
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminAccountRepository adminAccountRepository;
    private final LoyaltyTierRepository loyaltyTierRepository;
    private JavaMailSender javaMailSender;
    private JwtService jwtService;

    //Record OTP(Key: Email, Value: OTP)
    private final Map<String,String>otpStorage=new HashMap<>();

    public AuthService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder,
                       AdminAccountRepository adminAccountRepository, LoyaltyTierRepository loyaltyTierRepository,
                       JavaMailSender javaMailSender, JwtService jwtService){
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminAccountRepository = adminAccountRepository;
        this.loyaltyTierRepository = loyaltyTierRepository;
        this.javaMailSender=javaMailSender;
        this.jwtService = jwtService;

    }

//    Register Customer
    public Customer registerCustomer(RegisterRequest request){
        if(customerRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new RuntimeException("This phone number is already logged in the system!");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return customerRepository.save(customer);
    }

//Login
    public AuthResponse login(LoginRequest request){
//        TH1: Check admin
        var adminOpt = adminAccountRepository.findByUsername((request.getLoginKey()));
        if(adminOpt.isPresent()){
            var admin =adminOpt.get();
            if(!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
                throw new RuntimeException("The administrator account password is incorrect!");
            }
            String roleName = "ROLE_"+ admin.getRole().toUpperCase();
            String token =
                    jwtService.generateToken(
                            admin.getUsername(),
                            roleName
                    );

            return new AuthResponse(
                    admin.getAdminId(),
                    admin.getUsername(),
                    admin.getFullName(),
                    roleName,
                    token
            );
        }

//        TH2: Check customer
        var customerOpt = customerRepository.findByEmail(request.getLoginKey());
        if(customerOpt.isPresent()){
            var customer = customerOpt.get();
            if (customer.getPassword() == null) {
                throw new RuntimeException("This account was created via Google. Please log in using Google!");
            }
            if(!passwordEncoder.matches(request.getPassword(), customer.getPassword())){
                throw new RuntimeException("The customer account password is incorrect!");
            }
            String roleName = "ROLE_CUSTOMER";

            String token = jwtService.generateToken(customer.getEmail(), roleName);


            return new AuthResponse(
                    customer.getCustomerId(),
                    customer.getEmail(),
                    customer.getFullName(),
                    roleName,
                    token
            );
        }

//        TH3: No userName, phoneNumber matched
        throw new RuntimeException("The account email or administrator username does not exist!");
    }


//Create and Send OTP through Email
    public String generateAndCreateOtp(ForgotPasswordRequest request){
        Optional<Customer> customerOpt = customerRepository.findByEmail(request.getEmail());
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
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Error: User not found!"));

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
        otpStorage.remove(request.getEmail());
        return"Password changed successfully! You can now log in with your new password.";
    }

    // Google login
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        try {
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            // Xác thực Token với Google
            GoogleIdToken idToken = verifier.verify(request.getToken());
            if (idToken == null) {
                throw new RuntimeException("Google authentication failed: Invalid Token!");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Tìm hoặc Tự động đăng ký Customer mới bằng Email
            Customer customer = customerRepository.findByEmail(email).orElseGet(() -> {
                Customer newCustomer = new Customer();
                newCustomer.setFullName(name);
                newCustomer.setEmail(email);
                newCustomer.setPassword(null);
                newCustomer.setPhoneNumber(null);
                return customerRepository.save(newCustomer);
            });

            String roleName = "ROLE_CUSTOMER";
            // Sinh JWT hệ thống bằng Email của khách
            String token = jwtService.generateToken(customer.getEmail(), roleName);

            return new AuthResponse(
                    customer.getCustomerId(),
                    customer.getEmail(),
                    customer.getFullName(),
                    roleName,
                    token
            );
        } catch (Exception e) {
            throw new RuntimeException("Error processing Google login: " + e.getMessage());
        }
    }




}
