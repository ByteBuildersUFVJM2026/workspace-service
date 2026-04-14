package com.LigaAcademic.AcademicProject.User;

import java.time.LocalDate;

public record ContabHorasResponseDTO(
        float horas,
        String participantes,
        String setorAtividade,
        String descAtividade,
        String tipoAtividade,
        LocalDate dataAtividade
) {
}
