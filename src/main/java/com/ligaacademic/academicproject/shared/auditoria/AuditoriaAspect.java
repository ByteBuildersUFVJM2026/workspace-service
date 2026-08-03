package com.ligaacademic.academicproject.shared.auditoria;

import com.ligaacademic.academicproject.shared.auditoria.AuditoriaLog;
import com.ligaacademic.academicproject.shared.auditoria.AuditoriaRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditoriaAspect {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaAspect(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Around("@annotation(auditarAcao)")
    public Object registrarAuditoria(ProceedingJoinPoint joinPoint, AuditarAcao auditarAcao) throws Throwable {

        String email = obterUsuarioAutenticado();
        String nomeMetodo = joinPoint.getSignature().toShortString();

        try {
            Object resultado = joinPoint.proceed();

            salvarLogBanco(email, auditarAcao.acao(), nomeMetodo, "SUCESSO");

            return resultado;

        } catch (Exception e) {
            salvarLogBanco(email, auditarAcao.acao(), nomeMetodo, resumirFalha(e));

            throw e;
        }
    }

    private String obterUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName()))
                ? "Anônimo"
                : auth.getName();
    }


    private void salvarLogBanco(String email, String acao, String nomeMetodo, String status) {
        auditoriaRepository.save(AuditoriaLog.builder()
                .usuarioEmail(email)
                .acao(acao)
                .nomeMetodo(nomeMetodo)
                .status(status)
                .dataHora(LocalDateTime.now())
                .build());
    }

    private String resumirFalha(Exception exception) {
        String mensagem = exception.getMessage();
        if (mensagem == null || mensagem.isBlank()) {
            return "FALHA: " + exception.getClass().getSimpleName();
        }

        String status = "FALHA: " + mensagem;
        return status.length() <= 500 ? status : status.substring(0, 500);
    }
}
