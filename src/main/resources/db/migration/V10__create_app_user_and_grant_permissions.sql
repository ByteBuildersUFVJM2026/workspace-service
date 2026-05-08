-- Cria o usuário de aplicação (não-superusuário) se ainda não existir.
-- Credenciais injetadas via Flyway placeholders configurados no application.properties.
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '${app_user}') THEN
    CREATE USER ${app_user} WITH PASSWORD '${app_password}';
  END IF;
END
$$;

-- Acesso ao banco e ao schema
GRANT CONNECT ON DATABASE ligadb TO ${app_user};
GRANT USAGE ON SCHEMA public TO ${app_user};

-- DML completo em todas as tabelas existentes no momento da migration
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ${app_user};

-- Novas tabelas criadas por futures migrations herdam as mesmas permissões automaticamente
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${app_user};

-- Sequences (necessário caso alguma tabela use serial/bigserial no futuro)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ${app_user};

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO ${app_user};

-- auditoria_logs é append-only: registros de auditoria nunca podem ser alterados ou removidos
REVOKE DELETE, UPDATE ON TABLE auditoria_logs FROM ${app_user};

-- app_user não deve ter acesso à tabela interna de controle do Flyway
REVOKE ALL ON TABLE flyway_schema_history FROM ${app_user};