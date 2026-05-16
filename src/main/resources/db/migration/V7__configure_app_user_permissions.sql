-- Cria o usuário de aplicação com privilégios mínimos (sem acesso de superusuário).
-- Credenciais injetadas via Flyway placeholders configurados no application.properties.
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '${app_user}') THEN
    CREATE USER ${app_user} WITH PASSWORD '${app_password}';
  END IF;
END
$$;

GRANT CONNECT ON DATABASE ligadb TO ${app_user};
GRANT USAGE ON SCHEMA public TO ${app_user};

-- DML completo nas tabelas existentes no momento desta migration
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ${app_user};

-- Novas tabelas criadas por futuras migrations herdam as mesmas permissões
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${app_user};

-- Sequences (necessário para tabelas com BIGSERIAL)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ${app_user};

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO ${app_user};

-- auditoria_logs é append-only: o app nunca pode alterar ou deletar logs
REVOKE DELETE, UPDATE ON TABLE auditoria_logs FROM ${app_user};

-- O controle do Flyway é exclusivo do usuário admin do banco
REVOKE ALL ON TABLE flyway_schema_history FROM ${app_user};

-- Proteção adicional: nem o usuário postgres pode alterar registros de auditoria
REVOKE DELETE, UPDATE ON TABLE auditoria_logs FROM postgres;
