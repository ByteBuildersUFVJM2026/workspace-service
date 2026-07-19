package com.ligaacademic.academicproject.autenticacao.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank
        @Email(message = "Informe um email valido.")
        String email,

        @NotBlank
        String password
) {
}
