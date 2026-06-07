package com.ligaacademic.academicproject.dto;

import java.time.LocalDate;
import java.util.List;

public record RegistroAtividadesResponseDTO(

        Long id,
        float horas,
        List<String> participantes,
        String setorAtividade,
        String descAtividade,
        String tipoAtividade,
        LocalDate dataAtividade
) {
}

