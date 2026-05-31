package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.WashServiceRequest;
import com.swp391.autowashpro.dto.WashServiceResponse;
import com.swp391.autowashpro.entity.WashService;
import com.swp391.autowashpro.repository.WashServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WashServiceService {
    public final WashServiceRepository washServiceRepository;

    public WashServiceService(WashServiceRepository washServiceRepository){
        this.washServiceRepository = washServiceRepository;
    }

//  Get all wash services (Admin view - sees everything)
    public List<WashServiceResponse>getAllWashServices(){
        return washServiceRepository.findAll().stream().map(WashServiceResponse::new).toList();
    }
//  Create a new wash service with duplicate name validation
    @Transactional
    public WashServiceResponse createService(WashServiceRequest request) {
        if (washServiceRepository.existsByServiceName(request.getServiceName())) {
            throw new RuntimeException("Wash service with name '" + request.getServiceName() + "' already exists!");
        }

        WashService washService = new WashService();
        washService.setServiceName(request.getServiceName());
        washService.setDescription(request.getDescription());
        washService.setPrice(request.getPrice());
        washService.setDurationMinutes(request.getDurationMinutes());

        if (request.getIsActive() != null) {
            washService.setIsActive(request.getIsActive());
        } else {
            washService.setIsActive(true);
        }

        WashService savedWashService = washServiceRepository.save(washService);

        return new WashServiceResponse(savedWashService);
    }

//  Update existing wash service
    @Transactional
    public WashServiceResponse updateService(Integer id, WashServiceRequest request) {
        WashService service = washServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wash service not found with ID: " + id));

        service.setServiceName(request.getServiceName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDurationMinutes(request.getDurationMinutes());

        if (request.getIsActive() != null) {
            service.setIsActive(request.getIsActive());
        }

        return new WashServiceResponse(washServiceRepository.save(service));
    }

//  Delete a wash service completely
    @Transactional
    public void deleteService(Integer id) {
        if (!washServiceRepository.existsById(id)) {
            throw new RuntimeException("Wash service not found with ID: " + id);
        }
        washServiceRepository.deleteById(id);
    }

}
