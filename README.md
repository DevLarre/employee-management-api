# Employee Manager API

REST API para gerenciamento de funcionários desenvolvida com Java 21 e Spring Boot 3. O projeto demonstra boas práticas de desenvolvimento de APIs REST, incluindo arquitetura em camadas, validações, tratamento de erros, documentação com Swagger e testes unitários.

---

## Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Validation**
- **H2 Database** (desenvolvimento)
- **PostgreSQL / Neon** (produção)
- **Lombok**
- **SpringDoc OpenAPI 3 (Swagger)**
- **JUnit 5 + Mockito**

---

## Arquitetura

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:

```
controller/     → recebe requisições HTTP, delega ao service
service/        → regras de negócio, validações, logs
repository/     → acesso ao banco de dados via JPA
entity/         → mapeamento das tabelas do banco
dto/            → objetos de entrada e saída da API
mapper/         → conversão entre entity e dto
exception/      → exceções customizadas e handler global
enums/          → tipos enumerados
config/         → configurações (OpenAPI)
```

### Decisões de design

**Records para DTOs** — os DTOs são implementados como `record` do Java 21, garantindo imutabilidade e eliminando a necessidade de Lombok nos objetos de transferência.

**Mapper dedicado** — a conversão entre entidade e DTO é responsabilidade exclusiva do `EmployeeMapper`, mantendo entidades e DTOs desacoplados entre si.

**Separação de DTOs por operação** — `EmployeeCreateDto` para criação, `EmployeeUpdateDto` para atualização e `EmployeeResponseDto` para saída, cada um com apenas os campos e validações pertinentes à sua operação.

**Constraints na entidade e no DTO** — validações de entrada no DTO retornam erros amigáveis ao cliente; constraints na entidade (`@Column(nullable = false)`) garantem integridade no banco mesmo em chamadas diretas ao service.

---

## Estrutura de pacotes

```
src/
├── main/
│   ├── java/com/example/employee_manager_api/
│   │   ├── config/
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/
│   │   │   └── EmployeeController.java
│   │   ├── dto/
│   │   │   ├── AddressCreateDto.java
│   │   │   ├── AddressResponseDto.java
│   │   │   ├── EmployeeCreateDto.java
│   │   │   ├── EmployeeResponseDto.java
│   │   │   └── EmployeeUpdateDto.java
│   │   ├── entity/
│   │   │   ├── Address.java
│   │   │   └── Employee.java
│   │   ├── enums/
│   │   │   └── EmployeeStatus.java
│   │   ├── exception/
│   │   │   ├── BusinessException.java
│   │   │   ├── ErrorResponse.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── ValidationErrorResponse.java
│   │   ├── mapper/
│   │   │   └── EmployeeMapper.java
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java
│   │   └── service/
│   │       └── EmployeeService.java
│   └── resources/
│       ├── application.properties
│       └── application-prod.properties
└── test/
    └── java/com/example/employee_manager_api/
        └── service/
            └── EmployeeServiceTest.java
```

---

## Endpoints

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/employees` | Cadastrar funcionário | 201 |
| GET | `/api/employees` | Listar paginado | 200 |
| GET | `/api/employees/{id}` | Buscar por ID | 200 |
| GET | `/api/employees/status/{status}` | Filtrar por status | 200 |
| GET | `/api/employees/search?name=` | Buscar por nome | 200 |
| PUT | `/api/employees/{id}` | Atualizar funcionário | 200 |
| DELETE | `/api/employees/{id}` | Deletar funcionário | 204 |

### Status de funcionário disponíveis

`ATIVO` `INATIVO` `AFASTADO` `DEMITIDO`

---

## Como executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Rodando localmente com H2

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/employee-manager-api.git
cd employee-manager-api

# Compilar e executar
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`

### Acessos locais

| Interface | URL |
|-----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |
| API Docs | http://localhost:8080/api-docs |

**H2 Console — configurações de conexão:**
```
JDBC URL:  jdbc:h2:mem:employeedb
Username:  sa
Password:  (deixar em branco)
```

### Rodando com PostgreSQL / Neon

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Configure as variáveis do banco em `application-prod.properties` antes de executar.

---

## Exemplos de requisição

### Cadastrar funcionário

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "cpf": "123.456.789-09",
    "position": "Desenvolvedor Backend",
    "salary": 7500.00,
    "status": "ATIVO",
    "admissionDate": "2024-03-01",
    "address": {
      "street": "Rua das Flores",
      "number": "123",
      "neighborhood": "Centro",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01001-000"
    }
  }'
```

### Listar funcionários paginado

```bash
curl "http://localhost:8080/api/employees?page=0&size=10&sort=name"
```

### Buscar por status

```bash
curl "http://localhost:8080/api/employees/status/ATIVO?page=0&size=10"
```

### Buscar por nome

```bash
curl "http://localhost:8080/api/employees/search?name=João"
```

### Atualizar funcionário

```bash
curl -X PUT http://localhost:8080/api/employees/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Atualizado",
    "position": "Tech Lead",
    "salary": 12000.00,
    "status": "ATIVO"
  }'
```

---

## Tratamento de erros

A API retorna respostas padronizadas para todos os cenários de erro.

### Erro de recurso não encontrado — 404

```json
{
  "status": 404,
  "message": "Employee not found",
  "timestamp": "2024-03-28T22:00:00"
}
```

### Erro de negócio — 409

```json
{
  "status": 409,
  "message": "CPF Já cadastrado",
  "timestamp": "2024-03-28T22:00:00"
}
```

### Erro de validação — 400

```json
{
  "status": 400,
  "message": "Erro de validação",
  "errors": {
    "name": "Nome é obrigatório",
    "salary": "Salário deve ser positivo"
  },
  "timestamp": "2024-03-28T22:00:00"
}
```

---

## Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com relatório de cobertura
./mvnw test jacoco:report
```

Os testes cobrem a camada de service com os seguintes cenários:

- Criação de funcionário com sucesso
- Criação com CPF duplicado lança `BusinessException`
- Busca por ID existente retorna DTO
- Busca por ID inexistente lança `ResourceNotFoundException`
- Listagem paginada retorna página com funcionários
- Listagem retorna página vazia corretamente
- Atualização com sucesso
- Atualização com ID inexistente lança `ResourceNotFoundException`
- Deleção com sucesso
- Deleção com ID inexistente lança `ResourceNotFoundException`

---

## Evoluções previstas

- [ ] Autenticação e autorização com Spring Security + JWT
- [ ] Envio de e-mail ao cadastrar funcionário
- [ ] Exportação de relatório em PDF/Excel
- [ ] Auditoria de alterações com Hibernate Envers
- [ ] Paginação com filtros dinâmicos via Specification
- [ ] Testes de integração na camada controller

---

## Autor

Desenvolvido como projeto de portfólio para demonstração de habilidades em desenvolvimento de APIs REST com Java e Spring Boot.
