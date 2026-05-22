ALTER TABLE contabilhoras DROP COLUMN participantes;
ALTER TABLE contabilhoras DROP COLUMN matricula;

CREATE TABLE membro_atividade (
    atividade_id BIGINT NOT NULL REFERENCES contabilhoras(id) ON DELETE CASCADE,
    membro_id    BIGINT NOT NULL REFERENCES membros(id)       ON DELETE CASCADE,
    PRIMARY KEY (atividade_id, membro_id)
);
