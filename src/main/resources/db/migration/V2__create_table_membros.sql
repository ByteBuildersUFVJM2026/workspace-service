CREATE TABLE membros (
    id        BIGSERIAL    PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    cargo     VARCHAR(255) NOT NULL,
    matricula VARCHAR(11)  NOT NULL UNIQUE,
    email     VARCHAR(255) NOT NULL,

    CONSTRAINT membros_matricula_format CHECK (matricula ~ '^[0-9]{11}$'),
    CONSTRAINT membros_email_format     CHECK (email LIKE '%@%')
);
