ALTER TABLE guildas
    DROP CONSTRAINT guildas_quantidade_positiva;

ALTER TABLE guildas
    ADD CONSTRAINT guildas_quantidade_nao_negativa
        CHECK (quantidade_pessoas >= 0);
