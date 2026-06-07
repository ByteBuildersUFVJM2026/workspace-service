package com.ligaacademic.academicproject.mapper;

import com.ligaacademic.academicproject.dto.MembroRequestDTO;
import com.ligaacademic.academicproject.dto.MembroResponseDTO;
import com.ligaacademic.academicproject.model.GuildasModel;
import com.ligaacademic.academicproject.model.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MembroMapper {

    Membro paraEntidade(MembroRequestDTO dto);

    @Mapping(target = "guildas", source = "guildasModel", qualifiedByName = "guildaParaNomes")
    MembroResponseDTO paraResponseDTO(Membro membro);

    @Named("guildaParaNomes")
    default List<String> guildaParaNomes(List<GuildasModel> guildas) {
        if (guildas == null) return List.of();
        return guildas.stream().map(GuildasModel::getNome_guilda).toList();
    }
}
