-- Nomes derivados pelo SpringPhysicalNamingStrategy (camelCase → snake_case):
-- usuarioEmail → usuario_email | nomeMetodo → nome_metodo | dataHora → data_hora
-- Colunas sem NOT NULL: usuário anônimo e falhas podem gerar registros parciais.
CREATE TABLE auditoria_logs (
    id            UUID      PRIMARY KEY,
    usuario_email VARCHAR(255),
    acao          VARCHAR(255),
    nome_metodo   VARCHAR(500),
    data_hora     TIMESTAMP
);
