package com.example.employee_manager_api.dto;

import com.example.employee_manager_api.entity.Adress;
import com.example.employee_manager_api.enums.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponseDto (
        UUID id,
        String name,
        String cpf,
        String position,
        BigDecimal salary,
        EmployeeStatus status,
        LocalDate admissionDate,
        Adress adress
) {}