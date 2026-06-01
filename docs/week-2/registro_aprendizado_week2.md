# Registro de Aprendizado - Semana 2

Este documento será nosso diário de bordo da Semana 2.

A cada decisão, explicação, implementação, validação ou ajuste importante, vamos registrar:

- o que foi decidido;
- por que foi decidido;
- quais arquivos foram ou serão afetados;
- como explicar tecnicamente a decisão;
- como validar se deu certo;
- quais dúvidas ainda ficaram.

## Registro 01 - Visualização do banco sem alterar o projeto

Sim. A melhor implementação aqui é **não implementar nada dentro do projeto**.

O que vamos fazer é usar uma ferramenta visual externa, tipo **DBeaver**, conectada ao PostgreSQL que o projeto já sobe com Docker. Assim você consegue ver as tabelas, dados e mudanças em tempo real, sem alterar arquitetura, código, dependências ou configuração da aplicação.

**Como explicar para seu techlead**

Você pode dizer assim:

> Para acompanhar visualmente a evolução do banco durante a mentoria, vamos usar uma ferramenta cliente de PostgreSQL, como DBeaver, conectada ao banco Docker já existente. Isso não altera o projeto, não adiciona dependências e não muda a arquitetura. O PostgreSQL continua sendo o banco oficial, o Flyway continua versionando o schema, e a aplicação continua acessando o banco pelo datasource configurado. A ferramenta será usada apenas para inspeção, debug e aprendizado.

O ponto principal é este:

```text
Aplicação Spring Boot -> PostgreSQL Docker
DBeaver              -> PostgreSQL Docker
```

Os dois acessam o mesmo banco, mas o DBeaver é só uma janela visual. Ele não entra no código.

**Por que isso é melhor que H2**

Com H2, precisaríamos adicionar dependência, talvez criar profile, configurar outro datasource e cuidar de diferenças entre H2 e PostgreSQL. Mesmo que desse para fazer direito, seria uma camada extra.

Com DBeaver:

- não mexemos no `pom.xml`;
- não mexemos no `application.properties`;
- não mexemos na Clean Architecture;
- não mexemos nos UseCases;
- não criamos banco alternativo;
- vemos exatamente o banco real usado pelo projeto.

Ou seja: é mais fiel à mentoria.

**Como usar**

Depois que o PostgreSQL estiver rodando com Docker, você abre o DBeaver e cria uma conexão PostgreSQL com estes dados:

```text
Host: localhost
Port: 5433
Database: orderdb
Username: postgres
Password: postgres
```

Esses dados vêm do seu projeto:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/orderdb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

E do `docker-compose.yaml`:

```yaml
POSTGRES_DB: orderdb
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres
ports:
  - "5433:5432"
```

Depois de conectar, você vai conseguir ver:

- schema `public`;
- tabela `products`;
- tabela `flyway_schema_history`;
- colunas da tabela;
- constraints;
- registros criados;
- alterações feitas pelos endpoints.

**Como vamos usar na Semana 2**

Quando criarmos o `POST /products`, você poderá fazer uma requisição criando produto e depois abrir o DBeaver para ver a linha aparecer na tabela `products`.

Quando fizermos o `PUT /products/{id}`, você verá os campos mudarem.

Quando fizermos o `PATCH /products/{id}/stock`, você verá apenas o estoque mudar.

Quando fizermos o `DELETE /products/{id}`, você verá o registro sumir, se usarmos delete físico.

Então ele vira nosso painel de auditoria visual.

Minha recomendação: vamos instalar/abrir o DBeaver ou verificar se ele já está instalado, conectar no PostgreSQL do Docker e validar que conseguimos ver `flyway_schema_history` e `products`.

**Ponto de aprovação**

Posso seguir com essa etapa de verificação sem alterar o projeto?

## Registro 02 - Como rodar o projeto atual com PostgreSQL e DBeaver

Objetivo:

Rodar a cópia de trabalho da Semana 2 sem alterar código, usando o PostgreSQL real do Docker e o DBeaver apenas como ferramenta visual.

Pasta de trabalho:

```text
C:\Users\fferr\Desktop\guilherme_mentoria\2ºencontro\informacoesParaModeloConceitual\start_week2\order-management-fran
```

Comando para subir o banco:

```powershell
docker compose -f docker\docker-compose.yaml up -d
```

Comando para conferir se o container subiu:

```powershell
docker ps
```

O container esperado é:

```text
order-db
```

Comando para rodar a aplicação pela raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternativa pelo IntelliJ:

1. Abrir o projeto `start_week2/order-management-fran`.
2. Abrir a classe `OrderManagementApplication`.
3. Clicar no botão de Run ao lado do método/classe principal.
4. Aguardar a aplicação subir na porta `8080`.

URLs para validar:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/products?page=0&size=10
```

Conexão no DBeaver:

```text
Database: PostgreSQL
Host: localhost
Port: 5433
Database: orderdb
Username: postgres
Password: postgres
```

Tabelas esperadas no schema `public`:

- `products`;
- `flyway_schema_history`.

Observação sobre commits:

Rodar Docker, abrir DBeaver e testar endpoint não exige commit.
Quando começarmos a alterar código da Semana 2, os commits deverão seguir o padrão semântico exigido:

```text
refactor(product): separate domain model from persistence entity
feat(product): add POST /products with validation
feat(product): add GET /products/{id} with not found handling
feat(product): add PUT /products update flow
feat(product): add PATCH /products/{id}/stock endpoint
feat(product): add DELETE /products endpoint
docs(week-2): document product crud validation steps
```

## Registro 03 - Criando a conexão PostgreSQL no DBeaver

Estado:

O DBeaver foi instalado e aberto.
A tela inicial mostra uma conexão de exemplo chamada `DBeaver Sample Database (SQLite)`.
Essa conexão é apenas exemplo e não será usada no projeto.

Próximo objetivo:

Criar uma nova conexão PostgreSQL apontando para o banco Docker do projeto.

Passos no DBeaver:

1. Clicar no ícone de tomada com sinal de `+`, no canto superior esquerdo.
2. Escolher `PostgreSQL`.
3. Clicar em `Próximo`.
4. Preencher:

```text
Host: localhost
Port: 5433
Database: orderdb
Username: postgres
Password: postgres
```

5. Clicar em `Testar conexão`.
6. Se o DBeaver pedir para baixar o driver PostgreSQL, aceitar.
7. Se o teste der certo, clicar em `Concluir`.

Antes ou durante esse passo, o banco precisa estar rodando:

```powershell
docker compose -f docker\docker-compose.yaml up -d
```

Critério de sucesso:

No painel esquerdo do DBeaver deve aparecer a conexão PostgreSQL do projeto.
Dentro dela, devemos conseguir navegar até:

```text
Databases
orderdb
Schemas
public
Tables
```

Tabelas esperadas:

- `products`
- `flyway_schema_history`

Observação:

Não vamos usar a conexão SQLite de exemplo.
Ela pode ficar ali sem problema, mas não representa o banco do projeto.

Resultado validado:

O DBeaver conectou com sucesso no PostgreSQL do Docker.
A conexão exibiu:

```text
orderdb
public
Tables
flyway_schema_history
products
```

Interpretação:

- `flyway_schema_history` confirma que o Flyway está controlando as migrations.
- `products` confirma que a migration `V1__create_products_table.sql` criou a tabela principal da Semana 1.
- O DBeaver está conectado no mesmo banco usado pela aplicação Spring Boot.
- Nenhuma alteração foi feita no código do projeto para obter essa visualização.

Como explicar:

> Validamos a infraestrutura real do projeto. O PostgreSQL está rodando em Docker, o Flyway criou as tabelas e o DBeaver está sendo usado apenas como cliente visual para inspecionar o schema e os dados.

## Registro 04 - Git bloqueou a cópia por ownership diferente

Ao tentar rodar `git status` dentro de:

```text
start_week2/order-management-fran
```

o Git retornou:

```text
fatal: detected dubious ownership in repository
```

Motivo:

A pasta foi criada/copiada pelo ambiente do Codex, mas agora está sendo usada pelo usuário real do Windows.
Por segurança, o Git bloqueia operações quando o dono da pasta é diferente do usuário atual.

Correção recomendada pelo próprio Git:

```powershell
git config --global --add safe.directory 'C:/Users/fferr/Desktop/guilherme_mentoria/2ºencontro/informacoesParaModeloConceitual/start_week2/order-management-fran'
```

Interpretação:

- Isso não altera código do projeto.
- Isso não cria commit.
- Isso não muda branch.
- Isso apenas informa ao Git que esse diretório é confiável para o usuário atual.

## Registro 05 - Regra de explicação antes de cada mudança

Decisão:

A partir de agora, toda explicação técnica da Semana 2 deve ser documentada e ensinada no formato de auditoria.

Formato obrigatório:

```text
1. Vá neste arquivo
2. Veja que hoje ele está assim
3. Isso resulta neste problema ou consequência
4. O que o techlead/material pede
5. Para resolver, precisamos fazer isto
6. Posso seguir?
```

Motivo:

Esse formato ajuda a transformar cada mudança em aprendizado.
Em vez de apenas alterar código, vamos construir uma justificativa técnica para cada decisão.

Uso futuro:

- estudar antes da reunião com o techlead;
- explicar por que cada alteração existe;
- montar a descrição da PR da Semana 2;
- mostrar que as mudanças seguem o material da mentoria;
- evitar commits sem contexto.

Exemplo aplicado:

```text
Vá em ListProductsUseCase.java.
Hoje ele importa Product da camada infrastructure.persistence.entity.
Isso faz application depender de infrastructure.
O material pede que application conheça domain, não infraestrutura.
Para resolver, precisamos criar Product em domain/model e separar ProductEntity na persistência.
Antes de alterar, pedimos aprovação.
```

Regra prática:

Nenhuma alteração estrutural da Semana 2 será feita sem que essa análise seja apresentada antes.

## Registro 06 - Etapa 1: separar Domain Model de Entity JPA

Objetivo:

Corrigir a fronteira arquitetural entre `application` e `infrastructure`.

### Antes da alteração

Arquivo:

```text
src/main/java/br/com/devpasso/order_management/application/usecase/product/ListProductsUseCase.java
```

Estava assim:

```java
import br.com.devpasso.order_management.infrastructure.persistence.entity.Product;
```

Arquivo:

```text
src/main/java/br/com/devpasso/order_management/application/port/out/ProductRepositoryPort.java
```

Também estava assim:

```java
import br.com.devpasso.order_management.infrastructure.persistence.entity.Product;
```

Consequência:

A camada `application` dependia de uma classe da camada `infrastructure`.
Isso fazia o UseCase trabalhar diretamente com uma Entity JPA.

Problema arquitetural:

```text
application -> infrastructure
```

Esse fluxo fere a regra central da Clean Architecture usada na mentoria.

### O que o material pede

O material da mentoria define:

```text
Domain não conhece ninguém.
Application só conhece Domain.
Infrastructure conhece todos.
```

Portanto:

- UseCase deve trabalhar com modelo de domínio;
- JPA Entity deve ficar restrita à persistência;
- Adapter deve converter entre domínio e persistência.

### O que foi feito

Criado:

```text
src/main/java/br/com/devpasso/order_management/domain/model/Product.java
```

Esse arquivo agora representa o `Product` de domínio.
Ele é um `record` Java puro, sem anotação de Spring ou JPA.

Criado:

```text
src/main/java/br/com/devpasso/order_management/infrastructure/persistence/entity/ProductEntity.java
```

Esse arquivo agora representa a tabela `products`.
Ele tem:

```java
@Entity
@Table(name = "products")
```

Criado:

```text
src/main/java/br/com/devpasso/order_management/infrastructure/persistence/mapper/ProductPersistenceMapper.java
```

Responsabilidade:

```text
Product domain <-> ProductEntity JPA
```

Alterado:

```text
ProductRepositoryPort.java
ListProductsUseCase.java
ProductRepositoryAdapter.java
ProductJpaRepository.java
ProductWebMapper.java
```

### Depois da alteração

Agora `application` importa:

```java
import br.com.devpasso.order_management.domain.model.Product;
```

E não importa mais:

```java
br.com.devpasso.order_management.infrastructure.persistence.entity.Product
```

O fluxo ficou:

```text
Controller
-> UseCase
-> ProductRepositoryPort
-> ProductRepositoryAdapter
-> ProductPersistenceMapper
-> ProductEntity
-> ProductJpaRepository
```

Na volta:

```text
ProductEntity
-> ProductPersistenceMapper
-> Product domain
-> ProductWebMapper
-> ProductResponse
```

### Validação feita

Foi executada busca por imports antigos.
Resultado:

- `application` agora usa `domain.model.Product`;
- `infrastructure.persistence.entity.ProductEntity` ficou restrito à persistência;
- `ProductWebMapper` passou a receber `Product` de domínio.

Tentativa de rodar:

```powershell
cmd /c mvnw.cmd test
```

Resultado:

```text
Cannot start maven from wrapper
```

Interpretação:

O Maven wrapper falhou antes de iniciar o Maven, pelo mesmo problema já observado no ambiente.
Essa falha não veio da alteração de código.

Validação pendente:

Rodar pelo IntelliJ ou terminal local:

```powershell
.\mvnw.cmd test
```

Commit esperado:

```text
refactor(product): separate domain model from persistence entity
```

### Correção durante validação - construtores duplicados com Lombok

Ao tentar compilar pelo IntelliJ, apareceu:

```text
constructor ListProductsUseCase(...) is already defined
constructor ProductController(...) is already defined
```

Arquivos envolvidos:

```text
ListProductsUseCase.java
ProductController.java
```

O que havia:

As classes tinham `@RequiredArgsConstructor` e também construtor manual.

Exemplo do problema:

```java
@RequiredArgsConstructor
public class ListProductsUseCase {

    private final ProductRepositoryPort repository;

    public ListProductsUseCase(ProductRepositoryPort repository) {
        this.repository = repository;
    }
}
```

Consequência:

O Lombok gera automaticamente um construtor com os campos `final`.
Como o construtor manual tinha a mesma assinatura, o Java encontrou dois construtores iguais.

O que o material pede:

Usar `@RequiredArgsConstructor` como padrão para injeção por construtor.

Correção aplicada:

Removidos os construtores manuais e mantido o Lombok.

Arquivos corrigidos:

```text
src/main/java/br/com/devpasso/order_management/application/usecase/product/ListProductsUseCase.java
src/main/java/br/com/devpasso/order_management/infrastructure/web/controller/ProductController.java
```

Como explicar:

> A correção mantém o padrão do projeto com Lombok. Como `@RequiredArgsConstructor` já gera o construtor necessário para os campos `final`, o construtor manual era redundante e causava erro de compilação.

## Registro 07 - Etapa 2: expandir ProductRepositoryPort para o CRUD

Objetivo:

Preparar a camada `application` para os casos de uso da Semana 2 sem fazer UseCase conhecer Spring Data JPA.

### Antes da alteração

Arquivo:

```text
src/main/java/br/com/devpasso/order_management/application/port/out/ProductRepositoryPort.java
```

Estava assim:

```java
public interface ProductRepositoryPort {

    Page<Product> findAll(Pageable pageable);

}
```

Consequência:

O contrato de persistência só permitia listar produtos.
Isso era suficiente para a Semana 1, mas não para a Semana 2.

### O que a Semana 2 pede

A Semana 2 precisa dos endpoints:

```text
POST /products
GET /products/{id}
PUT /products/{id}
PATCH /products/{id}/stock
DELETE /products/{id}
```

Para isso, os UseCases precisam conseguir:

- salvar produto;
- buscar produto por ID;
- verificar nome duplicado;
- listar produtos;
- deletar produto.

### O que o material/techlead pede

O UseCase deve depender de interface, não de implementação concreta.

Forma correta:

```text
UseCase -> ProductRepositoryPort
Adapter -> ProductJpaRepository
```

Forma errada:

```text
UseCase -> ProductJpaRepository
```

### O que foi feito

Alterado:

```text
ProductRepositoryPort.java
```

Agora o contrato possui:

```java
Product save(Product product);
Optional<Product> findById(UUID id);
boolean existsByName(String name);
Page<Product> findAll(Pageable pageable);
void delete(Product product);
```

Alterado:

```text
ProductJpaRepository.java
```

Adicionado:

```java
boolean existsByNameIgnoreCase(String name);
```

Motivo:

O Spring Data JPA consegue implementar esse método automaticamente pelo nome.
Ele será usado para validar regra de negócio de nome duplicado.

Alterado:

```text
ProductRepositoryAdapter.java
```

Agora ele implementa os novos métodos do Port e faz as conversões:

```text
Product domain -> ProductEntity -> banco
banco -> ProductEntity -> Product domain
```

### Resultado arquitetural

`application` continua sem conhecer `ProductEntity`.

O JPA continua restrito à infraestrutura:

```text
application/port/out/ProductRepositoryPort.java -> domain.model.Product
infrastructure/persistence/adapter -> converte domain/entity
infrastructure/persistence/repository -> ProductEntity
```

### Validação feita

Foi feita busca por imports de infraestrutura.
Resultado:

- `ProductRepositoryPort` usa `domain.model.Product`;
- `ProductRepositoryAdapter` usa `ProductPersistenceMapper`;
- `ProductJpaRepository` usa `ProductEntity`;
- não foi introduzida dependência de JPA nos UseCases.

Commit esperado:

```text
refactor(product): expand product repository port for crud
```

## Registro 08 - Etapa 3: criar erro padronizado da API

Objetivo:

Criar a base de tratamento de erro antes dos novos UseCases da Semana 2.

### Antes da alteração

Foi feita busca por:

```text
Exception
ErrorResponse
RestControllerAdvice
BusinessException
ResourceNotFound
```

Resultado:

Não havia tratamento de erro padronizado no projeto.

Consequência:

Ao criar endpoints como:

```text
GET /products/{id}
POST /products
PUT /products/{id}
DELETE /products/{id}
```

os erros poderiam ficar espalhados no Controller ou cair em respostas genéricas do Spring.

### O que o material pede

A Semana 2 pede:

```text
GlobalExceptionHandler
ErrorResponse
ResourceNotFoundException
BusinessException
```

Com status:

```text
400 - erro de validação
404 - recurso não encontrado
409 - conflito/regra de negócio
```

### O que foi criado

Criado:

```text
src/main/java/br/com/devpasso/order_management/domain/exception/BusinessException.java
```

Uso esperado:

```text
Nome de produto duplicado -> 409 Conflict
```

Criado:

```text
src/main/java/br/com/devpasso/order_management/domain/exception/ResourceNotFoundException.java
```

Uso esperado:

```text
Produto inexistente -> 404 Not Found
```

Criado:

```text
src/main/java/br/com/devpasso/order_management/infrastructure/web/response/ErrorResponse.java
```

Contrato de erro:

```java
public record ErrorResponse(String code, String message) {}
```

Criado:

```text
src/main/java/br/com/devpasso/order_management/infrastructure/web/handler/GlobalExceptionHandler.java
```

Responsabilidade:

Converter exceções esperadas em JSON padronizado.

### Respostas padronizadas

`ResourceNotFoundException`:

```json
{
  "code": "NOT_FOUND",
  "message": "Produto não encontrado: ..."
}
```

`BusinessException`:

```json
{
  "code": "BUSINESS_ERROR",
  "message": "Produto com nome ... já existe"
}
```

`MethodArgumentNotValidException`:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "[name: não deve estar em branco]"
}
```

### Resultado arquitetural

O Controller não precisa tratar exceções manualmente.
Os UseCases podem lançar exceções de domínio/aplicação e a camada web transforma isso em HTTP.

Fluxo:

```text
UseCase lança exceção
-> GlobalExceptionHandler captura
-> API responde JSON padronizado
```

### Observação para a próxima etapa

Quando criarmos os Request DTOs com `@Valid`, talvez seja necessário adicionar a dependência:

```xml
spring-boot-starter-validation
```

Isso será analisado antes da etapa de DTOs.

Commit esperado:

```text
feat(error): add standardized api error handling
```
