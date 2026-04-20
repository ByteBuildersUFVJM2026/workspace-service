package com.LigaAcademic.AcademicProject.DTO;

import java.time.LocalDate;

public record ContabHorasResponseDTO(

        Long id,
        float horas,
        String participantes,
        String setorAtividade,
        String descAtividade,
        String tipoAtividade,
        LocalDate dataAtividade,
        String matricula
) {
}
