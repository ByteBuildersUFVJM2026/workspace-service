package com.ligaacademic.academicproject.guildas.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GuildasRequestDTO(
        @NotBlank
        String tutor_guilda,

        @NotBlank
        String nome_guilda,

        @Positive
        int quantidade_pessoas

) {
}

