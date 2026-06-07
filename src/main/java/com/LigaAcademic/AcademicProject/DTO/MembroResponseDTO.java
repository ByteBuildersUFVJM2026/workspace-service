package com.ligaacademic.academicproject.dto;

import java.util.List;

public record MembroResponseDTO(
        String nome,
        String matricula,
        String cargo,
        String email,
        float totalHoras,
        List<String> guildas
) {
}

