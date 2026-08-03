package com.ligaacademic.academicproject.shared.auditoria;

import com.ligaacademic.academicproject.shared.auditoria.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaLog, UUID> {
}

