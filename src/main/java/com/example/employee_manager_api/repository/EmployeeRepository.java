package com.example.employee_manager_api.repository;

import com.example.employee_manager_api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    boolean existsByCpf(String cpf);

}