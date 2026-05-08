-- Proteção de imutabilidade: logs de auditoria não podem ser alterados ou removidos.
-- Apenas INSERT e SELECT são permitidos para o usuário da aplicação.
REVOKE DELETE, UPDATE ON TABLE auditoria_logs FROM postgres;