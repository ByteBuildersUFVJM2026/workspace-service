package com.ligaacademic.academicproject.guildas.api;

import jakarta.validation.constraints.Positive;

public record GuildasQuantidadePessoasRequestDTO(
        @Positive
        int quantidade_pessoas
) {
}
