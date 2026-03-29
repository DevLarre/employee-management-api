package com.example.employee_manager_api.repository;

import com.example.employee_manager_api.entity.Employee;
import com.example.employee_manager_api.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    boolean existsByCpf(String cpf);

    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);

    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
}