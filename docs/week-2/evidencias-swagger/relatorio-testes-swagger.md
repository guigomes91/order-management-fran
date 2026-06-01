# Relatorio de Testes pelo Swagger - Semana 2

URL testada:

```text
http://localhost:8080/swagger-ui/index.html#/
```

## Contexto

Os testes desta pasta servem como evidencia de validacao da Semana 2.
O objetivo e provar que os endpoints foram testados visualmente pelo Swagger e que os resultados foram conferidos no fluxo da aplicacao.

## Teste inicial - POST /products

Payload:

```json
{
  "name": "Notebook Dell",
  "description": "Notebook para desenvolvimento Java",
  "price": 4500.00,
  "stockQuantity": 10
}
```

Resultado observado inicialmente:

```text
500 Internal Server Error
```

Diagnostico:

```text
null value in column "created_at" of relation "products" violates not-null constraint
```

Causa:

O `CreateProductUseCase` criava o produto com `createdAt = null`.
Como o Hibernate enviava `created_at = null` no INSERT, o `DEFAULT NOW()` do PostgreSQL nao era aplicado.

Correcao aplicada:

```text
CreateProductUseCase passou a usar LocalDateTime.now() ao criar produto.
```

Commit:

```text
fix(product): set createdAt when creating product
```

## Observacoes sobre uso do Swagger

### GET /products

Evitar executar com:

```text
sort=["string"]
```

Esse valor e apenas exemplo do Swagger e causa:

```text
No property '["string"]' found for type 'ProductEntity'
```

Usar:

```text
/products?page=0&size=10
```

Ou:

```text
/products?page=0&size=10&sort=name,asc
```

### PUT/PATCH/DELETE com id

Os endpoints com `{id}` exigem um UUID real retornado pelo `POST /products`.

Fluxo correto:

```text
1. Executar POST /products
2. Copiar o campo id retornado
3. Usar esse id em GET /products/{id}, PUT, PATCH e DELETE
```

## Checklist de validacao final

- [x] Swagger abriu corretamente
- [x] POST /products retornou 201
- [x] GET /products retornou 200
- [x] GET /products/{id} retornou 200
- [x] PUT /products/{id} retornou 200 mantendo estoque
- [x] PATCH /products/{id}/stock retornou 200 alterando apenas estoque
- [x] DELETE /products/{id} retornou 204
- [x] POST duplicado retornou 409
- [x] GET id inexistente retornou 404
- [x] POST invalido retornou 400
- [ ] DBeaver confirmou alteracoes na tabela products

## Evidencia automatica - Swagger aberto

Resultado:

```text
Swagger UI aberto em http://localhost:8080/swagger-ui/index.html#/
Products exibido com endpoints de CRUD.
```

Endpoints visualizados:

```text
GET /products/{id}
PUT /products/{id}
DELETE /products/{id}
GET /products
POST /products
PATCH /products/{id}/stock
```

## Rodada final pelo Swagger apos restart da aplicacao

Data/hora aproximada:

```text
01/06/2026 18:46-18:50 America/Sao_Paulo
```

Produto usado:

```text
Notebook Dell Swagger UI 1780350404688
```

ID criado:

```text
ac2f419e-df0d-4f06-8fce-b55908de07d1
```

### 01 - POST /products

Payload:

```json
{
  "name": "Notebook Dell Swagger UI 1780350404688",
  "description": "Notebook criado pelo teste visual do Swagger",
  "price": 4500,
  "stockQuantity": 10
}
```

Resultado:

```text
201 Created
```

Resposta observada:

```json
{
  "id": "ac2f419e-df0d-4f06-8fce-b55908de07d1",
  "name": "Notebook Dell Swagger UI 1780350404688",
  "description": "Notebook criado pelo teste visual do Swagger",
  "price": 4500,
  "stockQuantity": 10,
  "createdAt": "2026-06-01T18:46:53.7383435"
}
```

Conclusao:

Correcao do `createdAt` validada.

### 02 - GET /products

Request URL:

```text
http://localhost:8080/products?page=0&size=10
```

Resultado:

```text
200 OK
```

Resposta observada:

```text
content contem o produto criado
totalElements = 1
pageSize = 10
```

Observacao:

Foi removido o exemplo `sort=["string"]` do campo pageable, pois esse valor e apenas placeholder do Swagger e nao representa uma propriedade real da entidade.

### 03 - GET /products/{id}

ID usado:

```text
ac2f419e-df0d-4f06-8fce-b55908de07d1
```

Resultado:

```text
200 OK
```

Conclusao:

Busca por ID funcionando para produto existente.

### 04 - PUT /products/{id}

Payload:

```json
{
  "name": "Notebook Dell Swagger UI 1780350404688 Atualizado",
  "description": "Notebook atualizado pelo teste visual do Swagger",
  "price": 4700
}
```

Resultado:

```text
200 OK
```

Resposta observada:

```json
{
  "id": "ac2f419e-df0d-4f06-8fce-b55908de07d1",
  "name": "Notebook Dell Swagger UI 1780350404688 Atualizado",
  "description": "Notebook atualizado pelo teste visual do Swagger",
  "price": 4700,
  "stockQuantity": 10,
  "createdAt": "2026-06-01T18:46:53.738344"
}
```

Conclusao:

PUT atualizou dados principais e manteve o estoque em `10`, conforme regra da Semana 2.

### 05 - PATCH /products/{id}/stock

Payload:

```json
{
  "quantity": 7
}
```

Resultado:

```text
200 OK
```

Resposta observada:

```json
{
  "id": "ac2f419e-df0d-4f06-8fce-b55908de07d1",
  "name": "Notebook Dell Swagger UI 1780350404688 Atualizado",
  "description": "Notebook atualizado pelo teste visual do Swagger",
  "price": 4700,
  "stockQuantity": 7,
  "createdAt": "2026-06-01T18:46:53.738344"
}
```

Conclusao:

PATCH alterou somente o estoque.

### 06 - POST /products duplicado

Payload:

```json
{
  "name": "Notebook Dell Swagger UI 1780350404688 Atualizado",
  "description": "Produto duplicado pelo teste visual do Swagger",
  "price": 4700,
  "stockQuantity": 1
}
```

Resultado:

```text
409 Conflict
```

Resposta observada:

```json
{
  "code": "BUSINESS_ERROR",
  "message": "Produto com nome 'Notebook Dell Swagger UI 1780350404688 Atualizado' ja existe"
}
```

Conclusao:

Regra de nome duplicado validada.

### 07 - POST /products invalido

Payload:

```json
{
  "name": "",
  "description": "Payload invalido pelo Swagger",
  "price": 0,
  "stockQuantity": -1
}
```

Resultado:

```text
400 Bad Request
```

Resposta observada:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "[price: deve ser maior que ou igual a 0.01, stockQuantity: deve ser maior ou igual a 0, name: nao deve estar em branco, name: tamanho deve ser entre 3 e 255]"
}
```

Conclusao:

Bean Validation e `GlobalExceptionHandler` funcionando.

### 08 - DELETE /products/{id}

ID usado:

```text
ac2f419e-df0d-4f06-8fce-b55908de07d1
```

Resultado:

```text
204 No Content
```

Conclusao:

Delete fisico funcionando e retornando status correto.

### 09 - GET /products/{id} apos DELETE

ID usado:

```text
ac2f419e-df0d-4f06-8fce-b55908de07d1
```

Resultado:

```text
404 Not Found
```

Resposta observada:

```json
{
  "code": "NOT_FOUND",
  "message": "Produto nao encontrado: ac2f419e-df0d-4f06-8fce-b55908de07d1"
}
```

Conclusao:

Busca de produto removido retorna erro padronizado.
