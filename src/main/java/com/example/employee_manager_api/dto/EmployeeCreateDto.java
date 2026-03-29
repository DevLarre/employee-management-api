package com.example.employee_manager_api.dto;

import com.example.employee_manager_api.enums.EmployeeStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateDto(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "The name must not be more than 150 characters long.")
        String name,

        @NotBlank(message = "CPF is required")
        @Size(max = 14, message = "CPF invalid")
        String cpf,

        @NotBlank(message = "Position is required")
        @Size(max = 100, message = "The job title must have a maximum of 100 characters.")
        String position,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be positive")
        BigDecimal salary,

        @NotNull(message = "Status is requirede")
        EmployeeStatus status,

        @NotNull(message = "Admission date is required.")
        LocalDate admissionDate,

        @NotNull(message = "Address is required")
        @Valid
        AddressCreateDto address

) {}