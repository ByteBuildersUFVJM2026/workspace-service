package com.ligaacademic.academicproject.mapper;

import com.ligaacademic.academicproject.dto.RegistroAtividadesRequestDTO;
import com.ligaacademic.academicproject.dto.RegistroAtividadesResponseDTO;
import com.ligaacademic.academicproject.model.Membro;
import com.ligaacademic.academicproject.model.RegistroAtividades;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroAtividadesMapper {

    @Mapping(target = "participantes", ignore = true)
    RegistroAtividades horasParaEntidade(RegistroAtividadesRequestDTO registroAtividadesRequestDTO);

    RegistroAtividadesResponseDTO horasParaResponseDTO(RegistroAtividades registroAtividades);

    default String membroParaMatricula(Membro membro) {
        return membro.getMatricula();
    }
}

