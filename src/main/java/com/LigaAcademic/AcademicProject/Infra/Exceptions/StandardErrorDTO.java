package com.ligaacademic.academicproject.infra.exceptions;

import java.time.Instant;

public record StandardErrorDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}

