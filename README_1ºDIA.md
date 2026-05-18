# README — 1º DIA — WEEK-1

## Objetivo do dia

Executar integralmente a estrutura inicial da Semana 1 da mentoria OMS:
- setup do ambiente
- alinhamento arquitetural
- Docker/PostgreSQL
- Flyway
- persistência
- fluxo HTTP GET /products

---

# 1. Setup inicial

## Clone do projeto

```bash
git clone https://github.com/guigomes91/order-management-fran
````

## Criação da branch

```bash
git checkout -b week-1
```

---

# 2. Leitura arquitetural

Antes de criar código, foi realizada análise estrutural do projeto:

* src/main
* src/test
* OrderManagementApplication
* arquitetura baseada em Clean Architecture
* separação entre Application / Infrastructure

Objetivo:
entender o projeto antes de modificar qualquer arquivo.

---

# 3. Estrutura criada

## Infrastructure

* persistence/entity
* persistence/repository
* persistence/adapter
* web/controller
* web/response
* web/mapper

## Application

* port/out
* usecase/product

---

# 4. Migration Flyway

Arquivo criado:

```text
src/main/resources/db/migration/V1__create_products_table.sql
```

Migration criada para tabela `products`.

---

# 5. Problemas encontrados

## 5.1 Docker não iniciado

Erro:

```text
failed to connect to dockerDesktopLinuxEngine
```

Diagnóstico:
Docker Desktop não estava em execução.

Correção:
inicialização manual do Docker Desktop.

---

## 5.2 Flyway não executava

Inicialmente:

* aplicação subia
* PostgreSQL respondia
* mas nenhuma tabela era criada

Investigação realizada:

* dependency tree
* target/classes
* datasource
* pom.xml
* logs startup

Conclusão:
o projeto estava usando Spring Boot 4.0.6, divergindo da stack oficial da mentoria.

---

## 5.3 Conflito de porta PostgreSQL

Erro identificado:
aplicação conectava no PostgreSQL local do Windows e não no container Docker.

Diagnóstico realizado com:

```bash
netstat -ano | findstr :5432
```

Resultado:

* postgres.exe local
* Docker PostgreSQL
  usando simultaneamente a porta 5432.

Correção:
alteração do container para porta 5433.

---

## 5.4 Incompatibilidade Spring Boot 4 + Flyway

Erro encontrado:

```text
Unsupported Database: PostgreSQL 15.18
```

Conclusão:
stack desalinhada da mentoria.

Decisão:
alinhamento para Spring Boot 3.5.14 conforme documentação oficial.

---

# 6. Ajuste arquitetural

Versão alterada:

```xml
Spring Boot 4.0.6
↓
Spring Boot 3.5.14
```

Após alinhamento:

* Flyway executou corretamente
* migration aplicada
* schema versionado

---

# 7. Evidência Flyway

Logs relevantes:

```text
Successfully validated 1 migration
Migrating schema "public" to version "1 - create products table"
Successfully applied 1 migration
```

Validação direta no PostgreSQL:

```bash
docker exec -it order-db psql -U postgres -d orderdb -c "\dt"
```

Resultado:

```text
flyway_schema_history
products
```

---

# 8. Fluxo arquitetural implementado

Fluxo completo criado:

```text
HTTP GET /products
        ↓
ProductController
        ↓
ListProductsUseCase
        ↓
ProductRepositoryPort
        ↓
ProductRepositoryAdapter
        ↓
ProductJpaRepository
        ↓
PostgreSQL
```

---

# 9. Arquivos implementados

## Persistence

* Product.java
* ProductJpaRepository.java
* ProductRepositoryAdapter.java

## Application

* ProductRepositoryPort.java
* ListProductsUseCase.java

## Web

* ProductResponse.java
* ProductWebMapper.java
* ProductController.java

---

# 10. Endpoint validado

Endpoint:

```text
GET /products
```

Resultado:

* aplicação respondeu corretamente
* paginação funcionando
* JSON retornado
* Swagger funcionando

Resposta inicial esperada:

```json
{
  "content": []
}
```

---

# 11. Principais aprendizados

* troubleshooting Docker
* troubleshooting Flyway
* alinhamento de stack
* versionamento de schema
* Clean Architecture
* Ports & Adapters
* desacoplamento Application/Infrastructure
* UseCase como coração da aplicação
* DTO como boundary HTTP
* Spring Data JPA
* fluxo HTTP → banco

---

# 12. Status da Semana 1

| Item                   | Status |
| ---------------------- | ------ |
| Setup ambiente         | ✅      |
| Docker/PostgreSQL      | ✅      |
| Flyway                 | ✅      |
| Migration V1           | ✅      |
| Product Entity         | ✅      |
| Repository             | ✅      |
| Port/Adapter           | ✅      |
| UseCase                | ✅      |
| Mapper                 | ✅      |
| Controller             | ✅      |
| Endpoint GET /products | ✅      |
| Swagger                | ✅      |

---
