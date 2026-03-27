package com.example.employee_manager_api.dto;

import com.example.employee_manager_api.enums.EmployeeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.internal.build.AllowNonPortable;

import java.math.BigDecimal;

public record EmployeeUpdateDto (

        @NotBlank(message = "Name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Job position is required")
        @Size(max = 150)
        String position,

        @NotNull(message = "Wage is required")
        @Positive(message = "Wage must be positive")
        BigDecimal salary,

        @NotNull(message = "Status is required")
        EmployeeStatus status
) {}