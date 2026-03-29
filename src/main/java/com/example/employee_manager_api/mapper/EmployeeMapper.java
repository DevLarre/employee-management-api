package com.example.employee_manager_api.mapper;

import com.example.employee_manager_api.dto.AddressCreateDto;
import com.example.employee_manager_api.dto.AddressResponseDto;
import com.example.employee_manager_api.dto.EmployeeCreateDto;
import com.example.employee_manager_api.dto.EmployeeResponseDto;
import com.example.employee_manager_api.entity.Address;
import com.example.employee_manager_api.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeCreateDto dto) {
        return Employee.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .position(dto.position())
                .salary(dto.salary())
                .status(dto.status())
                .admissionDate(dto.admissionDate())
                .address(toAddressEntity(dto.address()))
                .build();
    }

    public EmployeeResponseDto toDto(Employee entity) {
        return new EmployeeResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.getPosition(),
                entity.getSalary(),
                entity.getStatus(),
                entity.getAdmissionDate(),
                toAddressDto(entity.getAddress())
        );
    }

    private Address toAddressEntity(AddressCreateDto dto) {
        return Address.builder()
                .street(dto.street())
                .number(dto.number())
                .neighborhood(dto.neighborhood())
                .city(dto.city())
                .state(dto.state())
                .zipCode(dto.zipCode())
                .build();
    }

    private AddressResponseDto toAddressDto(Address entity) {
        return new AddressResponseDto(
                entity.getStreet(),
                entity.getNumber(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode()
        );
    }
}