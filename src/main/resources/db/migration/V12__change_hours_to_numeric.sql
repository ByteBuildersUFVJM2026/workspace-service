ALTER TABLE membros
    ALTER COLUMN total_horas TYPE NUMERIC(10, 2) USING total_horas::NUMERIC(10, 2),
    ALTER COLUMN total_horas SET DEFAULT 0;

ALTER TABLE contabilhoras
    ALTER COLUMN horas TYPE NUMERIC(10, 2) USING horas::NUMERIC(10, 2);
