package com.LigaAcademic.AcademicProject.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record ContabHorasRequestDTO(

        @NotNull
        float horas,

        @NotBlank
        String participantes,

        @NotBlank
        String tipoAtividade,

        @NotBlank
        String descAtividade,

        @NotBlank
        String setorAtividade,

        @NotBlank
        @Pattern(regexp = "^[0-9]{11}$", message = "A matricula precisa ter exatamente 11 digitos. EX: 20222016003")
        String matricula,

        @NotNull
        @PastOrPresent
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataAtividade
) {
}
