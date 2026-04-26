CREATE TABLE auditoria_logs (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    usuario_email VARCHAR(255) NOT NULL,
    acao          VARCHAR(255) NOT NULL,
    nome_metodo   VARCHAR(255) NOT NULL,
    data_hora     TIMESTAMP    NOT NULL,

    CONSTRAINT pk_auditoria_logs PRIMARY KEY (id)
);

CREATE INDEX idx_auditoria_logs_data_hora ON auditoria_logs (data_hora);
