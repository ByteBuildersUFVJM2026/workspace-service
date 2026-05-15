-- Tabela de junção do relacionamento ManyToMany entre Membro e GuildasModel.
-- ON DELETE CASCADE garante limpeza automática quando um membro ou guilda é removido.
CREATE TABLE relacao_guildas (
    membro_id       BIGINT NOT NULL,
    guilda_model_id BIGINT NOT NULL,

    PRIMARY KEY (membro_id, guilda_model_id),

    CONSTRAINT fk_relacao_membro FOREIGN KEY (membro_id)
        REFERENCES membros (id) ON DELETE CASCADE,

    CONSTRAINT fk_relacao_guilda FOREIGN KEY (guilda_model_id)
        REFERENCES guildas (id) ON DELETE CASCADE
);
