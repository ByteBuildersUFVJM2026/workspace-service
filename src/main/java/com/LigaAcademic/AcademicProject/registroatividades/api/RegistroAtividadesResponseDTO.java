package com.ligaacademic.academicproject.registroatividades.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RegistroAtividadesResponseDTO(

        Long id,
        BigDecimal horas,
        List<String> participantes,
        String setorAtividade,
        String descAtividade,
        String tipoAtividade,
        LocalDate dataAtividade
) {
}

