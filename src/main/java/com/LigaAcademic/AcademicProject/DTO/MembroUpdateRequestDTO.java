package com.LigaAcademic.AcademicProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MembroUpdateRequestDTO(

        @NotBlank(message = "O nome não pode ser vazio.")
        String nome,

        @Email(message = "Adicione um email válido.")
        @NotBlank(message = "O email não pode ser vazio.")
        String email
) {
}