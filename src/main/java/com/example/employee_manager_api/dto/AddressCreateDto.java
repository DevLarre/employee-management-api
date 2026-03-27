package com.example.employee_manager_api.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressCreateDto(

        @NotBlank(message = "Rua é obrigatória")
        String street,

        @NotBlank(message = "Número é obrigatório")
        String number,

        @NotBlank(message = "Bairro é obrigatório")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatória")
        String city,

        @NotBlank(message = "Estado é obrigátoio")
        String state,

        @NotBlank(message = "CEP é obrigatório")
        String zipCode
) {}