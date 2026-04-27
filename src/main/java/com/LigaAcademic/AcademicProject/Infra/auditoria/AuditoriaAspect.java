package com.LigaAcademic.AcademicProject.Infra.auditoria;

import com.LigaAcademic.AcademicProject.model.AuditoriaLog;
import com.LigaAcademic.AcademicProject.repository.AuditoriaRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
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

    @AfterReturning("@annotation(auditarAcao)")
    public void registrarAuditoria(JoinPoint joinPoint, AuditarAcao auditarAcao) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName()))
                ? "Anônimo"
                : auth.getName();

        auditoriaRepository.save(AuditoriaLog.builder()
                .usuarioEmail(email)
                .acao(auditarAcao.acao())
                .nomeMetodo(joinPoint.getSignature().toShortString())
                .dataHora(LocalDateTime.now())
                .build());
    }
}