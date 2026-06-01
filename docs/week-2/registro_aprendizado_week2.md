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
