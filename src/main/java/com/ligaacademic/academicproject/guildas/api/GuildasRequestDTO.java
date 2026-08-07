package com.ligaacademic.academicproject.guildas.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GuildasRequestDTO(
        @NotBlank
        String tutor_guilda,

        @NotBlank
        String nome_guilda,

        @PositiveOrZero
        int quantidade_pessoas

) {
}

