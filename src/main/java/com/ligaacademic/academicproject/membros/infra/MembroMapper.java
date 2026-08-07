package com.ligaacademic.academicproject.membros.infra;

import com.ligaacademic.academicproject.membros.api.MembroRequestDTO;
import com.ligaacademic.academicproject.membros.api.MembroResponseDTO;
import com.ligaacademic.academicproject.guildas.domain.GuildasModel;
import com.ligaacademic.academicproject.membros.domain.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MembroMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guildasModel", ignore = true)
    @Mapping(target = "totalHoras", ignore = true)
    @Mapping(target = "fazParteDiretoria", source = "fazParteDiretoria")
    Membro paraEntidade(MembroRequestDTO dto);

    @Mapping(target = "guildas", source = "guildasModel", qualifiedByName = "guildaParaNomes")
    @Mapping(target = "fazParteDiretoria", source = "fazParteDiretoria")
    MembroResponseDTO paraResponseDTO(Membro membro);

    @Named("guildaParaNomes")
    default List<String> guildaParaNomes(List<GuildasModel> guildas) {
        if (guildas == null) return List.of();
        return guildas.stream().map(GuildasModel::getNomeGuilda).toList();
    }
}
