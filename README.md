# Backend - Liga Academica

API REST em Spring Boot 3.3 com Java 21. O backend usa PostgreSQL, Redis, Flyway, JWT e MinIO para armazenamento de documentos.

## Requisitos

- Docker e Docker Compose para o modo recomendado.
- Java 21 e Maven Wrapper, se for rodar a API fora do Docker.

## Rodando com Docker

O modo mais simples e usar o compose da raiz do projeto:

```bash
cd ..
docker compose up --build
```

Esse comando constroi a API, sobe PostgreSQL, Redis, MinIO e frontend, e deixa a API disponivel em:

```text
http://localhost:8080
```

## Portas

| Servico | Porta local | Uso |
| --- | ---: | --- |
| API Spring Boot | 8080 | Endpoints REST |
| PostgreSQL | 5432 | Banco `ligadb` |
| Redis | 6379 | Cache/rate limit |
| MinIO API | 9000 | Storage de arquivos |
| MinIO Console | 9001 | Painel web do MinIO |

## Variaveis obrigatorias

O compose da raiz ja tem valores padrao para desenvolvimento. Para personalizar, copie `.env.example` da raiz para `.env`.

Variaveis principais:

| Variavel | Descricao |
| --- | --- |
| `DB_URL` | URL JDBC do PostgreSQL |
| `DB_USERNAME` / `DB_PASSWORD` | Usuario admin usado pelo Flyway |
| `USER_APP` / `PASSWORD_APP` | Usuario da aplicacao criado pelas migrations |
| `JWT_SECRET` | Segredo usado para assinar tokens JWT |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | Credenciais do primeiro usuario admin |
| `REDIS_HOST` / `REDIS_PORT` | Conexao com Redis |
| `MINIO_ENDPOINT` | URL interna ou local do MinIO |
| `MINIO_ACCESS_KEY` / `MINIO_SECRET_KEY` | Credenciais do MinIO |
| `MINIO_BUCKET` | Bucket usado para documentos |
| `CORS_ALLOWED_ORIGINS` | Origem permitida para o frontend |

## Rodando somente a infraestrutura

Se quiser rodar a API pelo Maven no host e apenas os servicos auxiliares no Docker, use o compose de desenvolvimento que ja existe neste diretorio:

```bash
docker compose -f docker-compose.dev.yml up -d
```

Depois suba a API:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Nesse modo, a API usa o perfil `dev` e espera PostgreSQL em `localhost:5432`, Redis em `localhost:6379` e MinIO em `localhost:9000`.

## Banco e migrations

As migrations do Flyway ficam em:

```text
src/main/resources/db/migration
```

Ao subir a API pela primeira vez, o Flyway cria/valida as tabelas e aplica a configuracao do usuario da aplicacao.
