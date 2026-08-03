package com.ligaacademic.academicproject.documentos.api;

import org.springframework.core.io.InputStreamResource;

public record DocumentDownloadDTO(
        InputStreamResource resource,
        String originalFileName,
        String contentType,
        Long fileSize
) {
}
