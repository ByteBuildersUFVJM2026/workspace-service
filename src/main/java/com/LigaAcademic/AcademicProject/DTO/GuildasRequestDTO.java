package com.LigaAcademic.AcademicProject.DTO;

import jakarta.validation.constraints.NotBlank;

public record GuildasRequestDTO(
        @NotBlank
        String tutor_guildas,

        @NotBlank
        String nome_guilda,

        @NotBlank
        int quantidade_pessoas

) {
}
