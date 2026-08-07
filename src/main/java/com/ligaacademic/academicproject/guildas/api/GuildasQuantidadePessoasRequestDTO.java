package com.ligaacademic.academicproject.guildas.api;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GuildasQuantidadePessoasRequestDTO(
        @PositiveOrZero
        int quantidade_pessoas
) {
}
