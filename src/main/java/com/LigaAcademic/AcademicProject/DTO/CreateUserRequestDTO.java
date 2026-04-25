package com.LigaAcademic.AcademicProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank
        @Email(message = "Informe um email válido.")
        String email,

        @NotBlank
        String password
) {}