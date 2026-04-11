package com.LigaAcademic.AcademicProject.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.antlr.v4.runtime.misc.NotNull;

public record MembroRequestDTO(
        @NotBlank(message = "O nome não pode ser vazio.")
          String nome,

        @NotBlank(message = "O cargo não pode ser vazio.")
         String cargo,
        @Pattern(regexp = "^[0-9]{11}$", message = "A matricula precisa ter exatamente 11 digitos. EX: 20222016003")
        @NotBlank(message = "A matricula não pode ser vazio.")
         String matricula,

        @Email(message = "Adicione um email valido.")
        @NotBlank(message = "O email não pode ser vazio.")
         String email) {
}
