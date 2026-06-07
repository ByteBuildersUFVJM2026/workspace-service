package com.ligaacademic.academicproject.dto;

import jakarta.validation.constraints.Positive;

public record GuildasQuantidadePessoasRequestDTO(
        @Positive
        int quantidade_pessoas
) {
}
