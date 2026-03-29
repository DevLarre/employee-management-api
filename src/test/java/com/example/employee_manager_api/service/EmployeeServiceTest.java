package com.example.employee_manager_api.service;

import com.example.employee_manager_api.dto.*;
import com.example.employee_manager_api.entity.Address;
import com.example.employee_manager_api.entity.Employee;
import com.example.employee_manager_api.enums.EmployeeStatus;
import com.example.employee_manager_api.exception.BusinessException;
import com.example.employee_manager_api.exception.ResourceNotFoundException;
import com.example.employee_manager_api.mapper.EmployeeMapper;
import com.example.employee_manager_api.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeService service;

    private Employee employee;
    private EmployeeResponseDto responseDto;
    private EmployeeCreateDto createDto;
    private AddressCreateDto addressCreateDto;

    @BeforeEach
    void setUp() {
        addressCreateDto = new AddressCreateDto(
                "Rua das Flores", "123", "Centro",
                "Balneário Pinhal ", "Rio Grande Do Sul", "95599-000"
        );

        createDto = new EmployeeCreateDto(
                "João Silva", "123.456.789-09", "Desenvolvedor",
                new BigDecimal("5000.00"), EmployeeStatus.ATIVO,
                LocalDate.of(2024, 1, 15), addressCreateDto
        );

        Address address = Address.builder()
                .id(UUID.randomUUID())
                .street("Rua das Flores").number("123")
                .neighborhood("Centro").city("São Paulo")
                .state("SP").zipCode("01001-000")
                .build();

        employee = Employee.builder()
                .id(UUID.randomUUID())
                .name("João Silva").cpf("123.456.789-09")
                .position("Desenvolvedor").salary(new BigDecimal("5000.00"))
                .status(EmployeeStatus.ATIVO)
                .admissionDate(LocalDate.of(2024, 1, 15))
                .address(address)
                .build();

        responseDto = new EmployeeResponseDto(
                employee.getId(), "João Silva", "123.456.789-09",
                "Desenvolvedor", new BigDecimal("5000.00"),
                EmployeeStatus.ATIVO, LocalDate.of(2024, 1, 15),
                new AddressResponseDto("Rua das Flores", "123",
                        "Centro", "São Paulo", "SP", "01001-000")
        );
    }

    // ─── CREATE ───────────────────────────────────────────────

    @Test
    @DisplayName("Deve criar funcionário com sucesso")
    void create_success() {
        when(repository.existsByCpf(createDto.cpf())).thenReturn(false);
        when(mapper.toEntity(createDto)).thenReturn(employee);
        when(repository.save(employee)).thenReturn(employee);
        when(mapper.toDto(employee)).thenReturn(responseDto);

        EmployeeResponseDto result = service.create(createDto);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("João Silva");
        assertThat(result.cpf()).isEqualTo("123.456.789-09");

        verify(repository).existsByCpf(createDto.cpf());
        verify(repository).save(employee);
        verify(mapper).toDto(employee);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando CPF já cadastrado")
    void create_cpfDuplicado_throwsBusinessException() {
        when(repository.existsByCpf(createDto.cpf())).thenReturn(true);

        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("CPF Já cadastrado");

        verify(repository, never()).save(any());
    }

    // ─── FIND BY ID ───────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar funcionário quando ID existe")
    void findById_success() {
        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(mapper.toDto(employee)).thenReturn(responseDto);

        EmployeeResponseDto result = service.findById(employee.getId());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(employee.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existe")
    void findById_notFound_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found");

        verify(mapper, never()).toDto(any());
    }

    // ─── FIND ALL ─────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar página de funcionários")
    void findAll_success() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(employee));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(employee)).thenReturn(responseDto);

        Page<EmployeeResponseDto> result = service.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há funcionários")
    void findAll_empty() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(Page.empty());

        Page<EmployeeResponseDto> result = service.findAll(pageable);

        assertThat(result.getContent()).isEmpty();
        verify(mapper, never()).toDto(any());
    }

    // ─── UPDATE ───────────────────────────────────────────────

    @Test
    @DisplayName("Deve atualizar funcionário com sucesso")
    void update_success() {
        EmployeeUpdateDto updateDto = new EmployeeUpdateDto(
                "João Atualizado", "Arquiteto",
                new BigDecimal("8000.00"), EmployeeStatus.ATIVO
        );

        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(repository.save(employee)).thenReturn(employee);
        when(mapper.toDto(employee)).thenReturn(responseDto);

        EmployeeResponseDto result = service.update(employee.getId(), updateDto);

        assertThat(result).isNotNull();
        verify(repository).save(employee);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar ID inexistente")
    void update_notFound_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        EmployeeUpdateDto updateDto = new EmployeeUpdateDto(
                "João", "Dev", new BigDecimal("5000.00"), EmployeeStatus.ATIVO
        );

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, updateDto))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    // ─── DELETE ───────────────────────────────────────────────

    @Test
    @DisplayName("Deve deletar funcionário com sucesso")
    void delete_success() {
        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));

        service.delete(employee.getId());

        verify(repository).delete(employee);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar ID inexistente")
    void delete_notFound_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).delete(any());
    }
}