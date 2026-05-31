package com.swp391.autowashpro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WashServiceRequest {
    @NotBlank(message = "Service name cannot be blank")
    @Size(min = 3, max = 100, message = "Service name must be between 3 and 100 characters")
    private String serviceName;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration must be greater than 0")
    private Integer durationMinutes;

    private Boolean isActive;
}
