package com.ligaacademic.academicproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record RegistroAtividadesRequestDTO(

        @NotNull
        float horas,

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
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataAtividade
) {
}

