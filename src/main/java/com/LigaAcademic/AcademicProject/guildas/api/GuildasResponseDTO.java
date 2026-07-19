package com.ligaacademic.academicproject.guildas.api;

import java.util.List;

public record GuildasResponseDTO(
        Long id,
        String tutor_guilda,
        String nome_guilda,
        int quantidade_pessoas,
        List<String> membros
) {
}
