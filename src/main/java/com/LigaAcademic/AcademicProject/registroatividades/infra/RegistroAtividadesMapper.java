package com.ligaacademic.academicproject.registroatividades.infra;

import com.ligaacademic.academicproject.registroatividades.api.RegistroAtividadesRequestDTO;
import com.ligaacademic.academicproject.registroatividades.api.RegistroAtividadesResponseDTO;
import com.ligaacademic.academicproject.membros.domain.Membro;
import com.ligaacademic.academicproject.registroatividades.domain.RegistroAtividades;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroAtividadesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    RegistroAtividades horasParaEntidade(RegistroAtividadesRequestDTO registroAtividadesRequestDTO);

    RegistroAtividadesResponseDTO horasParaResponseDTO(RegistroAtividades registroAtividades);

    default String membroParaMatricula(Membro membro) {
        return membro.getMatricula();
    }
}

