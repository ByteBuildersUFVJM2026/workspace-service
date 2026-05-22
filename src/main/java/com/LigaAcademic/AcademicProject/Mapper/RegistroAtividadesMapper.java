package com.LigaAcademic.AcademicProject.Mapper;

import com.LigaAcademic.AcademicProject.DTO.RegistroAtividadesRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.RegistroAtividadesResponseDTO;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.model.RegistroAtividades;
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
