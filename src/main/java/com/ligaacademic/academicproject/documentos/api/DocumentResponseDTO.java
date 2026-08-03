package com.ligaacademic.academicproject.documentos.api;

import com.ligaacademic.academicproject.documentos.domain.DocumentSourceType;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentResponseDTO(
        UUID id,
        String title,
        String description,
        DocumentSourceType sourceType,
        String originalFileName,
        String contentType,
        Long fileSize,
        String externalUrl,
        UUID createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
