package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.StaffRequest;
import com.swp391.autowashpro.dto.StaffResponse;
import com.swp391.autowashpro.entity.AdminAccount;
import com.swp391.autowashpro.repository.AdminAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminAccountService {
    private final AdminAccountRepository adminAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public AdminAccountService(AdminAccountRepository adminAccountRepository, BCryptPasswordEncoder passwordEncoder){
        this.adminAccountRepository=adminAccountRepository;
        this.passwordEncoder =passwordEncoder;
    }
//  Get list of all staffs
    public List<StaffResponse> getAllStaffs(){
        return adminAccountRepository.findAllStaff().stream().map(StaffResponse::new).toList();
    }
//   Create a new staff
    public StaffResponse createStaff(StaffRequest request){
       if(adminAccountRepository.existsByUsername(request.getUsername())){
           throw new RuntimeException("This username is already registered!");
       }
       AdminAccount adminAccount = new AdminAccount();
       adminAccount.setFullName(request.getFullName());
       adminAccount.setRole("STAFF");
       adminAccount.setUsername(request.getUsername());
       adminAccount.setPassword(passwordEncoder.encode(request.getPassword()));

       return new StaffResponse(adminAccountRepository.save(adminAccount));
    }

//  Update staff info
    public StaffResponse updateStaff(Integer id, StaffRequest request){
        AdminAccount adminAccount = adminAccountRepository.findByAdminId(id)
                .orElseThrow(()-> new RuntimeException("Staff not found with ID: " + id));

        if(adminAccountRepository.existsByUsernameAndAdminIdNot(request.getUsername(), id)){
            throw new RuntimeException("This username is already registered");
        }

        adminAccount.setUsername(request.getUsername());
        adminAccount.setFullName(request.getFullName());
        adminAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        return new StaffResponse(adminAccountRepository.save(adminAccount));
    }

//  Delete staff
    public void deleteStaff(Integer id){
        AdminAccount account = adminAccountRepository.findByAdminId(id)
                .orElseThrow(() -> new RuntimeException("Cannot find the account with id:" + id+"!"));

        if (!"STAFF".equalsIgnoreCase(account.getRole())) {
            throw new RuntimeException("Do not have no authority to delete this manager account!");
        }
        adminAccountRepository.deleteById(id);
    }


}
