package com.LigaAcademic.AcademicProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GuildasRequestDTO(
        @NotBlank
        String tutor_guildas,

        @NotBlank
        String nome_guilda,

        @Positive
        int quantidade_pessoas

) {
}
