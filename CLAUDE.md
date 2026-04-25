- **Controllers**: Apenas camada HTTP — delegar toda lógica aos serviços
- **Services**: Lógica de negócio; usar Mappers para converter entre entidades e DTOs
- **Repositories**: Interfaces Spring Data JPA
- **Model**: Entidades JPA — nunca expostas diretamente em respostas da API
- **DTO**: Objetos de Requisição/Resposta para todos os contratos da API (obrigatório — nunca expor entidades)
- **Mapper**: Mappers MapStruct (gerados em tempo de compilação via annotation processor)
- **Infra/security**: Autenticação JWT com cadeia de filtros (`SecurityFilter` → `TokenService`), configuração Spring Security

## Modelo de Segurança

Autenticação JWT stateless. Dois papéis: `ROLE_ADMIN`, `ROLE_USER`. TTL do token: 2 horas.

- `POST /auth/login` — retorna JWT
- `POST /auth/register` — cria usuário
- Todos os outros endpoints requerem um header válido `Authorization: Bearer <token>`

## Entidades do Domínio

| Entidade | Tabela | Notas |
|---|---|---|
| `User` | `tb_users` | Spring Security `UserDetails`, PK UUID |
| `Membro` | (membros) | Possui `matricula` como chave de negócio |
| `GuildasModel` | `guildas` | Muitos-para-muitos com Membro via `relacao_guildas` |
| `ContabilHoras` | — | Rastreamento de atividades/horas |

## Convenções Principais

- Use **injeção de construtor** (sem `@Autowired` em injeção de campo)
- Use **recursos Java 21** quando apropriado (records, pattern matching, sealed classes)
- Sempre criar tanto `RequestDTO` quanto `ResponseDTO` por entidade
- Mappers MapStruct ficam no pacote `Mapper/` — adicione um novo mapper interface por entidade
- Novas mudanças de BD vão em um novo arquivo de migração Flyway (`V{n}__descricao.sql`)

### 🎯 O QUE PRECISA SER FEITO
[Resumo da tarefa]

### 🤔 POR QUE ESSA ABORDAGEM?
[AQUI É O MAIS IMPORTANTE]
- Por que escolhi A em vez de B
- Quais são os benefícios dessa decisão
- Que problemas essa solução evita
- Como alinha com as convenções do projeto
- Padrões de mercado que estou seguindo
- Por que essa é a abordagem mais escalável/segura

### 💻 O CÓDIGO
[Código aqui]

### ✅ PRÓXIMOS PASSOS
[O que vem depois]

### GITGUB

### COMMAND: [CREATE-PR]
**Trigger:** ONLY execute this workflow when the user explicitly types the exact keyword `/create-pr`. Ignore this workflow in all other conversational contexts.

**Workflow Action:** Automate the Pull Request creation strictly following GitHub Flow and Conventional Commits.
**Project Context:** Spring Boot REST API (Java 21) for an Academic League.

**Execution Steps (Require explicit user approval before steps 4, 5, and 6):**
1. **Analysis:** Run `git status` and `git diff` to understand the modifications.
2. **Branching:** Propose a semantic branch name (e.g., `feature/...`, `fix/...`, `refactor/...`). Once approved, create and checkout using `git checkout -b <branch-name>`.
3. **Staging:** Stage files (`git add .`).
4. **Commit:** Propose a semantic commit message explaining the *why*. Wait for approval.
5. **Push:** Push to remote (`git push -u origin <branch-name>`).
6. **Pull Request:** Use GitHub CLI (`gh pr create`). Title matches commit. Body must include: Context (Why), Changes (Technical list), and Checklist (Architecture compliance).

**Rule:** Start by executing Step 1 and proposing the branch name/commit message immediately after the user types `/create-pr`.