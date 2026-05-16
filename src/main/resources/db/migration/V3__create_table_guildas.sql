CREATE TABLE guildas (
    id                 BIGSERIAL    PRIMARY KEY,
    nome_guilda        VARCHAR(255) NOT NULL,
    tutor_guilda       VARCHAR(255) NOT NULL,
    quantidade_pessoas INTEGER      NOT NULL,

    CONSTRAINT guildas_quantidade_positiva CHECK (quantidade_pessoas > 0)
);
