package com.LigaAcademic.AcademicProject.Mapper;

import com.LigaAcademic.AcademicProject.DTO.GuildasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasResponseDTO;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.model.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuildasMapper {

    GuildasModel guildaParaEntidade(GuildasRequestDTO guildasRequestDTO);

    @Mapping(target = "membros", source = "membro", qualifiedByName = "membroParaNomes")
    GuildasResponseDTO guildaParaResponseDTO(GuildasModel guildasModel);

    @Named("membroParaNomes")
    default List<String> membroParaNomes(List<Membro> membros) {
        if (membros == null) return List.of();
        return membros.stream().map(Membro::getNome).toList();
    }
}