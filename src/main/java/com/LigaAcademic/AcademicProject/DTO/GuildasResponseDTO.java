package com.ligaacademic.academicproject.dto;

import java.util.List;

public record GuildasResponseDTO(
        Long id,
        String tutor_guilda,
        String nome_guilda,
        int quantidade_pessoas,
        List<String> membros
) {
}
