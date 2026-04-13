package com.LigaAcademic.AcademicProject.User;

public record MembroResponseDTO(
        Long id,
        String nome,
        String matricula,
        String cargo,
        String email
) {
}
