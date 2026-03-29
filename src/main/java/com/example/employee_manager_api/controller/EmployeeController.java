package com.example.employee_manager_api.controller;

import com.example.employee_manager_api.dto.EmployeeCreateDto;
import com.example.employee_manager_api.dto.EmployeeResponseDto;
import com.example.employee_manager_api.dto.EmployeeUpdateDto;
import com.example.employee_manager_api.enums.EmployeeStatus;
import com.example.employee_manager_api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee management")
public class EmployeeController {

    private final EmployeeService service;

    @Operation(summary = "Register employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "CPF already registered.")
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(@Valid @RequestBody EmployeeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Search employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "List employees paginated")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "List employees by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<EmployeeResponseDto>> findByStatus(
            @PathVariable EmployeeStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByStatus(status, pageable));
    }

    @Operation(summary = "Search employees by name")
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDto>> findByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByName(name, pageable));
    }

    @Operation(summary = "Update employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody EmployeeUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete employee")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}