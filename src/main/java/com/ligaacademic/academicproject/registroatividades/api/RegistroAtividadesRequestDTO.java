package com.ligaacademic.academicproject.registroatividades.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RegistroAtividadesRequestDTO(

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal horas,

        @NotEmpty
        List<String> matriculas,

        @NotBlank
        String tipoAtividade,

        @NotBlank
        String descAtividade,

        @NotBlank
        String setorAtividade,

        @NotNull
        @PastOrPresent
        LocalDate dataAtividade
) {
}

