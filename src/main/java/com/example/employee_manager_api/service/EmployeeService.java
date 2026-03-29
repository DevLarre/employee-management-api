package com.example.employee_manager_api.service;


import com.example.employee_manager_api.dto.EmployeeCreateDto;
import com.example.employee_manager_api.dto.EmployeeResponseDto;
import com.example.employee_manager_api.dto.EmployeeUpdateDto;
import com.example.employee_manager_api.entity.Employee;
import com.example.employee_manager_api.exception.BusinessException;
import com.example.employee_manager_api.exception.ResourceNotFoundException;
import com.example.employee_manager_api.mapper.EmployeeMapper;
import com.example.employee_manager_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<EmployeeResponseDto> findAll(Pageable pageable) {

        log.info("Listando funcionários com paginação. Página: {}, Tamanho: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAll(pageable)
                .map(mapper::toDto);
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

    public EmployeeResponseDto update(UUID id, EmployeeUpdateDto dto) {

        log.info("Atualizando funcionário ID: {}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Funcionário não encontrado para atualização ID: {}", id);
                    return new ResourceNotFoundException("Employee not found");
                });

        employee.setName(dto.name());
        employee.setPosition(dto.position());
        employee.setSalary(dto.salary());
        employee.setStatus(dto.status());

        Employee updated = repository.save(employee);

        log.info("Funcionário atualizado com sucesso. ID: {}", updated.getId());

        return mapper.toDto(updated);
    }

    public void delete(UUID id) {

        log.info("Removendo funcionário ID: {}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Funcionário não encontrado para remoção ID: {}", id);
                    return new ResourceNotFoundException("Employee not found");
                });
        repository.delete(employee);
        log.info("Funcionário removido com sucesso ID: {}", id);
    }
}