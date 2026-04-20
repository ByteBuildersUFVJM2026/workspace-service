package com.LigaAcademic.AcademicProject.DTO;

import jakarta.validation.constraints.Positive;

public record GuildasQuantidadePessoasRequestDTO(
        @Positive
        int quantidade_pessoas
) {
}