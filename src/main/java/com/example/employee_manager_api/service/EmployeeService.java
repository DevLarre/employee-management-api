package com.example.employee_manager_api.service;


import com.example.employee_manager_api.dto.EmployeeCreateDto;
import com.example.employee_manager_api.dto.EmployeeResponseDto;
import com.example.employee_manager_api.entity.Employee;
import com.example.employee_manager_api.exception.BusinessException;
import com.example.employee_manager_api.exception.ResourceNotFoundException;
import com.example.employee_manager_api.mapper.EmployeeMapper;
import com.example.employee_manager_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeResponseDto create(EmployeeCreateDto dto) {
        log.info("Criando funcionario CPF: {}", dto.cpf());

        if(repository.existsByCpf(dto.cpf())) {
            log.warn("Tentativa de cadastro com CPF já existente: {}", dto.cpf());
            throw new BusinessException("CPF Já cadastrado");
        }

        Employee saved = repository.save(mapper.toEntity(dto));

        log.info("Funcionário criado com sucesso. ID: {}", saved.getId());

        return mapper.toDto(saved);
    }

    public EmployeeResponseDto findById(UUID id) {

        log.info("Buscando funcionário ID: {}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Funcionário não encontrado ID: {}", id);
                    return new ResourceNotFoundException("Employee not found");
                });
        return mapper.toDto(employee);
    }

    public void delete(UUID id) {

        log.info("Removendo funcionário ID: ()", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Funcionário não encontrado para remoção ID: {}", id);
                    return new ResourceNotFoundException("Employee not found");
                });
        repository.delete(employee);
        log.info("Funcionário removido com sucesso ID: {}", id);
    }
}