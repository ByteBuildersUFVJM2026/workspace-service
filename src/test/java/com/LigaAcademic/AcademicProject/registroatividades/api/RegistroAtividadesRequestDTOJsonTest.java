package com.ligaacademic.academicproject.registroatividades.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RegistroAtividadesRequestDTOJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveDesserializarDataAtividadeNoFormatoIso() throws Exception {
        var json = """
                {
                  "horas": 2.5,
                  "matriculas": ["20260001"],
                  "tipoAtividade": "EXTENSAO",
                  "descAtividade": "Participacao em evento",
                  "setorAtividade": "Ensino",
                  "dataAtividade": "2026-07-29"
                }
                """;

        var request = objectMapper.readValue(json, RegistroAtividadesRequestDTO.class);

        assertThat(request.dataAtividade()).isEqualTo(LocalDate.of(2026, 7, 29));
    }
}
