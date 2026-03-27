package com.example.employee_manager_api.dto;

public record AddressResponseDto (
        String address,
        String street,
        String number,
        String neighborhood,
        String city,
        String state
){}
