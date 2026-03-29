# Employee Manager API

REST API para gerenciamento de funcionários desenvolvida com Java 21 e Spring Boot 3. O projeto demonstra boas práticas de desenvolvimento de APIs REST, incluindo arquitetura em camadas, validações, tratamento de erros, documentação com Swagger e testes unitários.

---

## Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Validation**
- **H2 Database** — banco em memória para desenvolvimento e avaliação local
- **PostgreSQL** — banco relacional para ambiente de produção
- **Neon** — PostgreSQL serverless para deploy em nuvem
- **Lombok**
- **SpringDoc OpenAPI 3 (Swagger)**
- **JUnit 5 + Mockito + AssertJ**

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

**Mapper dedicado** — a conversão entre entidade e DTO é responsabilidade exclusiva do `EmployeeMapper`, mantendo entidades e DTOs completamente desacoplados entre si. Nenhuma entidade conhece o DTO e nenhum DTO conhece a entidade.

**Separação de DTOs por operação** — `EmployeeCreateDto` para criação, `EmployeeUpdateDto` para atualização e `EmployeeResponseDto` para saída, cada um com apenas os campos e validações pertinentes à sua operação. CPF e data de admissão, por exemplo, não aparecem no DTO de atualização pois são imutáveis após o cadastro.

**Constraints na entidade e no DTO** — validações de entrada no DTO retornam erros 400 amigáveis ao cliente via `@NotBlank`, `@NotNull` e `@Positive`. Constraints na entidade (`@Column(nullable = false)`) garantem integridade no banco físico mesmo em chamadas diretas ao service.

**Address sem camada própria** — `Address` não tem identidade de negócio independente. Ele só existe vinculado a um `Employee` e é gerenciado pelo JPA via `CascadeType.ALL`, sem necessidade de controller, service ou repository próprios.

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
        ├── mapper/
        │   └── EmployeeMapperTest.java
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

### Status disponíveis

`ATIVO` `INATIVO` `AFASTADO` `DEMITIDO`

---

## Como executar

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Opção 1 — H2 em memória (sem instalação, ideal para avaliação)

```bash
git clone https://github.com/seu-usuario/employee-manager-api.git
cd employee-manager-api
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. O banco é criado automaticamente em memória e resetado a cada reinicialização.

| Interface | URL |
|-----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |
| API Docs | http://localhost:8080/api-docs |

**H2 Console — configurações:**
```
JDBC URL:  jdbc:h2:mem:employeedb
Username:  sa
Password:  (deixar em branco)
```

### Opção 2 — PostgreSQL local

Crie o banco e o usuário:

```sql
CREATE DATABASE employeedb;
CREATE USER employee_user WITH PASSWORD 'senha123';
GRANT ALL PRIVILEGES ON DATABASE employeedb TO employee_user;
```

Preencha o `application-prod.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employeedb
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=employee_user
spring.datasource.password=senha123

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.h2.console.enabled=false
```

Execute com o profile de produção:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Opção 3 — Neon (PostgreSQL serverless em nuvem)

1. Crie uma conta em [neon.tech](https://neon.tech)
2. Crie um novo projeto e copie a string de conexão
3. Preencha o `application-prod.properties`:

```properties
spring.datasource.url=jdbc:postgresql://ep-XXXX.us-east-2.aws.neon.tech/neondb?sslmode=require
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=seu_usuario_neon
spring.datasource.password=sua_senha_neon

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.h2.console.enabled=false
```

Execute:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

> O arquivo `application-prod.properties` está no `.gitignore`. Nunca suba credenciais reais no repositório.

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

### Filtrar por status

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

### Deletar funcionário

```bash
curl -X DELETE http://localhost:8080/api/employees/{id}
```

---

## Tratamento de erros

A API retorna respostas padronizadas para todos os cenários de erro.

### 404 — recurso não encontrado

```json
{
  "status": 404,
  "message": "Employee not found",
  "timestamp": "2024-03-28T22:00:00"
}
```

### 409 — conflito de negócio

```json
{
  "status": 409,
  "message": "CPF Já cadastrado",
  "timestamp": "2024-03-28T22:00:00"
}
```

### 400 — erro de validação

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
./mvnw test
```

### EmployeeServiceTest

| Cenário | Resultado esperado |
|---------|-------------------|
| Criar com sucesso | Retorna DTO com dados salvos |
| Criar com CPF duplicado | Lança `BusinessException` |
| Buscar por ID existente | Retorna DTO do funcionário |
| Buscar por ID inexistente | Lança `ResourceNotFoundException` |
| Listar paginado com dados | Retorna página com funcionários |
| Listar paginado sem dados | Retorna página vazia |
| Atualizar com sucesso | Retorna DTO atualizado |
| Atualizar ID inexistente | Lança `ResourceNotFoundException` |
| Deletar com sucesso | Executa sem erro |
| Deletar ID inexistente | Lança `ResourceNotFoundException` |

### EmployeeMapperTest

| Cenário | Resultado esperado |
|---------|-------------------|
| DTO para entidade | Todos os campos mapeados corretamente |
| Endereço no DTO para entidade | Campos do endereço mapeados |
| ID nulo ao converter DTO | Banco é quem gera o ID |
| Entidade para DTO | Todos os campos mapeados corretamente |
| Endereço na entidade para DTO | Campos do endereço mapeados |
| ID preservado na saída | ID da entidade aparece no ResponseDto |
| Consistência ida e volta | Campos batem entre create e response |

---

## Evoluções previstas

- [ ] Autenticação e autorização com Spring Security + JWT
- [ ] Envio de e-mail ao cadastrar funcionário
- [ ] Exportação de relatório em PDF/Excel
- [ ] Auditoria de alterações com Hibernate Envers
- [ ] Filtros dinâmicos via Specification
- [ ] Testes de integração na camada controller

---

## Autor

Desenvolvido como projeto de portfólio para demonstração de habilidades em desenvolvimento de APIs REST com Java e Spring Boot.
