package com.LigaAcademic.AcademicProject.User;

import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;

import java.time.LocalDate;

public record ContabHorasRequestDTO(

        @NotBlank
        float horas,

        @NotBlank
        String participantes,

        @NotBlank
        String tipoAtividade,

        @NotBlank
        String descAtividade,

        @NotBlank
        String setorAtividade,

        @NotBlank
        LocalDate dataAtividade
) {
}
