package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.WashService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.nio.file.WatchService;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WashServiceResponse {
    private Integer serviceId;
    private String serviceName;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private Boolean isActive;

    public WashServiceResponse(WashService washService) {
        this.serviceId= washService.getServiceId();
        this.serviceName = washService.getServiceName();
        this.description = washService.getDescription();
        this.price = washService.getPrice();
        this.durationMinutes = washService.getDurationMinutes();
        this.isActive = washService.getIsActive();
    }
}
