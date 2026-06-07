package com.ligaacademic.academicproject.repository;

import com.ligaacademic.academicproject.model.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaLog, UUID> {
}

