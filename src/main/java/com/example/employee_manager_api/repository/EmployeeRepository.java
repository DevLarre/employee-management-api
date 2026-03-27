package com.example.employee_manager_api.repository;

import com.example.employee_manager_api.entity.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByCpf(@NotBlank(message = "CPF é obrigatório") @Size(max = 14, message = "CPF inválido") String cpf);

    Employee save(Employee entity);

    Optional<Employee> findById(UUID id);
}
