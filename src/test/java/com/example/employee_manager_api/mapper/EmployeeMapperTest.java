package com.example.employee_manager_api.mapper;

import com.example.employee_manager_api.dto.*;
import com.example.employee_manager_api.entity.Address;
import com.example.employee_manager_api.entity.Employee;
import com.example.employee_manager_api.enums.EmployeeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    private final EmployeeMapper mapper = new EmployeeMapper();

    private AddressCreateDto addressCreateDto;
    private EmployeeCreateDto createDto;
    private Employee employee;
    private Address address;

    @BeforeEach
    void setUp() {
        addressCreateDto = new AddressCreateDto(
                "Rua das Flores",
                "123",
                "Centro",
                "São Paulo",
                "SP",
                "01001-000"
        );

        createDto = new EmployeeCreateDto(
                "João Silva",
                "123.456.789-09",
                "Desenvolvedor Backend",
                new BigDecimal("7500.00"),
                EmployeeStatus.ATIVO,
                LocalDate.of(2024, 1, 15),
                addressCreateDto
        );

        address = Address.builder()
                .id(UUID.randomUUID())
                .street("Rua das Flores")
                .number("123")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01001-000")
                .build();

        employee = Employee.builder()
                .id(UUID.randomUUID())
                .name("João Silva")
                .cpf("123.456.789-09")
                .position("Desenvolvedor Backend")
                .salary(new BigDecimal("7500.00"))
                .status(EmployeeStatus.ATIVO)
                .admissionDate(LocalDate.of(2024, 1, 15))
                .address(address)
                .build();
    }

    // ─── toEntity ────────────────────────────────────────────

    @Test
    @DisplayName("Deve converter EmployeeCreateDto para Employee corretamente")
    void toEntity_deveMapearTodosOsCampos() {
        Employee entity = mapper.toEntity(createDto);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("João Silva");
        assertThat(entity.getCpf()).isEqualTo("123.456.789-09");
        assertThat(entity.getPosition()).isEqualTo("Desenvolvedor Backend");
        assertThat(entity.getSalary()).isEqualByComparingTo(new BigDecimal("7500.00"));
        assertThat(entity.getStatus()).isEqualTo(EmployeeStatus.ATIVO);
        assertThat(entity.getAdmissionDate()).isEqualTo(LocalDate.of(2024, 1, 15));
    }

    @Test
    @DisplayName("Deve converter o endereço corretamente ao mapear para entidade")
    void toEntity_deveMapearEnderecoCorretamente() {
        Employee entity = mapper.toEntity(createDto);

        assertThat(entity.getAddress()).isNotNull();
        assertThat(entity.getAddress().getNumber()).isEqualTo("123");
        assertThat(entity.getAddress().getNeighborhood()).isEqualTo("Centro");
        assertThat(entity.getAddress().getCity()).isEqualTo("São Paulo");
        assertThat(entity.getAddress().getState()).isEqualTo("SP");
        assertThat(entity.getAddress().getZipCode()).isEqualTo("01001-000");
    }

    @Test
    @DisplayName("Não deve setar ID ao converter DTO para entidade — o banco gera o ID")
    void toEntity_naoDeveSetarId() {
        Employee entity = mapper.toEntity(createDto);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getAddress().getId()).isNull();
    }

    // ─── toDto ────────────────────────────────────────────────

    @Test
    @DisplayName("Deve converter Employee para EmployeeResponseDto corretamente")
    void toDto_deveMapearTodosOsCampos() {
        EmployeeResponseDto dto = mapper.toDto(employee);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(employee.getId());
        assertThat(dto.name()).isEqualTo("João Silva");
        assertThat(dto.cpf()).isEqualTo("123.456.789-09");
        assertThat(dto.position()).isEqualTo("Desenvolvedor Backend");
        assertThat(dto.salary()).isEqualByComparingTo(new BigDecimal("7500.00"));
        assertThat(dto.status()).isEqualTo(EmployeeStatus.ATIVO);
        assertThat(dto.admissionDate()).isEqualTo(LocalDate.of(2024, 1, 15));
    }

    @Test
    @DisplayName("Deve converter o endereço corretamente ao mapear para DTO")
    void toDto_deveMapearEnderecoCorretamente() {
        EmployeeResponseDto dto = mapper.toDto(employee);

        assertThat(dto.address()).isNotNull();
        assertThat(dto.address().number()).isEqualTo("123");
        assertThat(dto.address().neighborhood()).isEqualTo("Centro");
        assertThat(dto.address().city()).isEqualTo("São Paulo");
        assertThat(dto.address().state()).isEqualTo("SP");
        assertThat(dto.address().zipCode()).isEqualTo("01001-000");
    }

    @Test
    @DisplayName("Deve preservar o ID da entidade no DTO de resposta")
    void toDto_devePreservarId() {
        EmployeeResponseDto dto = mapper.toDto(employee);

        assertThat(dto.id()).isNotNull();
        assertThat(dto.id()).isEqualTo(employee.getId());
    }

    // ─── consistência ida e volta ─────────────────────────────

    @Test
    @DisplayName("Campos convertidos para entidade e de volta para DTO devem ser consistentes")
    void toEntityEToDto_devemSerConsistentes() {
        Employee entity = mapper.toEntity(createDto);
        // simula o banco setando um ID
        entity = employee;

        EmployeeResponseDto dto = mapper.toDto(entity);

        assertThat(dto.name()).isEqualTo(createDto.name());
        assertThat(dto.cpf()).isEqualTo(createDto.cpf());
        assertThat(dto.position()).isEqualTo(createDto.position());
        assertThat(dto.salary()).isEqualByComparingTo(createDto.salary());
        assertThat(dto.status()).isEqualTo(createDto.status());
        assertThat(dto.admissionDate()).isEqualTo(createDto.admissionDate());
    }
}