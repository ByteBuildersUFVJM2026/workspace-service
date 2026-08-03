package com.ligaacademic.academicproject.documentos.infra;

import com.ligaacademic.academicproject.documentos.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    Page<Document> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Document> findByIdAndDeletedAtIsNull(UUID id);
}
