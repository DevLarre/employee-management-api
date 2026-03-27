package com.example.employee_manager_api.dto;

import com.example.employee_manager_api.enums.EmployeeStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateDto(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String name,

        @NotBlank(message = "CPF é obrigatório")
        @Size(max = 14, message = "CPF inválido")
        String cpf,

        @NotBlank(message = "Cargo é obrigatório")
        @Size(max = 100, message = "Cargo deve ter no máximo 100 caracteres")
        String position,

        @NotNull(message = "Salário é obrigatório")
        @Positive(message = "Salário deve ser positivo")
        BigDecimal salary,

        @NotNull(message = "Status é obrigatório")
        EmployeeStatus status,

        @NotNull(message = "Data de admissão é obrigatória")
        LocalDate admissionDate,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        AddressCreateDto address

) {}