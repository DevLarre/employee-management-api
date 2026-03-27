package com.example.employee_manager_api.dto;

import jakarta.validation.constraints.NotBlank;


public record AddressCreateDto(

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "Number is required")
        String number,

        @NotBlank(message = "Neighborhood is required")
        String neighborhood,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "ZipCode is required")
        String zipCode
) {}