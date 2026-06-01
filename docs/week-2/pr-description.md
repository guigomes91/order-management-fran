# feat(week-2): implement full product CRUD with validation and error handling

## O que foi feito

- Separacao entre `Product` de dominio e `ProductEntity` JPA.
- Criacao de `ProductPersistenceMapper` para converter dominio e persistencia.
- Expansao do `ProductRepositoryPort` para suportar CRUD completo.
- Criacao de DTOs de request com Bean Validation.
- Criacao de Commands para desacoplar UseCases da camada web.
- Criacao de excecoes customizadas e `GlobalExceptionHandler`.
- Criacao dos UseCases de CRUD de produto.
- Atualizacao do `ProductController` com endpoints da Semana 2.
- Correcao do `createdAt` na criacao de produto.
- Documentacao da jornada em `docs/week-2/registro_aprendizado_week2.md`.
- Registro de evidencias de teste pelo Swagger em `docs/week-2/evidencias-swagger/relatorio-testes-swagger.md`.

## Endpoints implementados

| Metodo | Endpoint | Status esperado |
| --- | --- | --- |
| POST | `/products` | `201 Created` |
| GET | `/products` | `200 OK` |
| GET | `/products/{id}` | `200 OK` |
| PUT | `/products/{id}` | `200 OK` |
| PATCH | `/products/{id}/stock` | `200 OK` |
| DELETE | `/products/{id}` | `204 No Content` |

## Tratamento de erros

| Cenario | Status | Corpo |
| --- | --- | --- |
| Payload invalido | `400 Bad Request` | `VALIDATION_ERROR` |
| Produto inexistente | `404 Not Found` | `NOT_FOUND` |
| Nome duplicado | `409 Conflict` | `BUSINESS_ERROR` |

## Como testar

1. Subir o banco:

```powershell
docker compose -f docker\docker-compose.yaml up -d
```

2. Rodar a aplicacao pelo IntelliJ ou terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

3. Abrir Swagger:

```text
http://localhost:8080/swagger-ui/index.html#/
```

4. Executar os fluxos:

- `POST /products` com produto valido -> `201`
- `GET /products?page=0&size=10` -> `200`
- `GET /products/{id}` com id valido -> `200`
- `PUT /products/{id}` -> `200`, mantendo estoque
- `PATCH /products/{id}/stock` -> `200`, alterando somente estoque
- `POST /products` com nome duplicado -> `409`
- `POST /products` invalido -> `400`
- `DELETE /products/{id}` -> `204`
- `GET /products/{id}` apos delete -> `404`

## Evidencias

Validadas pelo Swagger UI:

```text
docs/week-2/evidencias-swagger/relatorio-testes-swagger.md
```

## Pontos de atencao

- No Swagger, evitar o placeholder `sort=["string"]` em `GET /products`, pois ele nao representa uma propriedade real.
- Para testar `PUT`, `PATCH` e `DELETE`, usar o UUID real retornado pelo `POST /products`.
- A tabela `products` pode ser inspecionada visualmente pelo DBeaver usando o PostgreSQL do Docker.
