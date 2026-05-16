-- Nomes de coluna definidos explicitamente via @Column na entidade ContabilHoras.
CREATE TABLE contabilhoras (
    id             BIGSERIAL    PRIMARY KEY,
    horas          REAL         NOT NULL,
    tipoatividade  VARCHAR(255) NOT NULL,
    setoratividade VARCHAR(255) NOT NULL,
    descatividade  TEXT         NOT NULL,
    dataatividade  DATE         NOT NULL,
    participantes  TEXT         NOT NULL,
    matricula      VARCHAR(11)  NOT NULL,

    CONSTRAINT contabilhoras_matricula_format CHECK (matricula ~ '^[0-9]{11}$')
);
