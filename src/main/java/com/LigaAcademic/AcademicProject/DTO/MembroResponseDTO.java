package com.LigaAcademic.AcademicProject.DTO;

import java.util.List;

public record MembroResponseDTO(
        Long id,
        String nome,
        String matricula,
        String cargo,
        String email,
        List<String> guildas
) {
}
