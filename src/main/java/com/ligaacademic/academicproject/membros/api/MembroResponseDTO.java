package com.ligaacademic.academicproject.membros.api;

import java.math.BigDecimal;
import java.util.List;

public record MembroResponseDTO(
        String nome,
        String matricula,
        String cargo,
        String email,
        BigDecimal totalHoras,
        List<String> guildas
) {
}

