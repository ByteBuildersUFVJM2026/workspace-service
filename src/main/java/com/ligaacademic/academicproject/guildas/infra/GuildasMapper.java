package com.ligaacademic.academicproject.guildas.infra;

import com.ligaacademic.academicproject.guildas.api.GuildasRequestDTO;
import com.ligaacademic.academicproject.guildas.api.GuildasResponseDTO;
import com.ligaacademic.academicproject.guildas.domain.GuildasModel;
import com.ligaacademic.academicproject.membros.domain.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuildasMapper {

    @Mapping(target = "nomeGuilda", source = "nome_guilda")
    @Mapping(target = "tutorGuilda", source = "tutor_guilda")
    @Mapping(target = "quantidadePessoas", source = "quantidade_pessoas")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "membro", ignore = true)
    GuildasModel guildaParaEntidade(GuildasRequestDTO guildasRequestDTO);

    @Mapping(target = "membros", source = "membro", qualifiedByName = "membroParaNomes")
    @Mapping(target = "nome_guilda", source = "nomeGuilda")
    @Mapping(target = "tutor_guilda", source = "tutorGuilda")
    @Mapping(target = "quantidade_pessoas", source = "quantidadePessoas")
    GuildasResponseDTO guildaParaResponseDTO(GuildasModel guildasModel);

    @Named("membroParaNomes")
    default List<String> membroParaNomes(List<Membro> membros) {
        if (membros == null) return List.of();
        return membros.stream().map(Membro::getNome).toList();
    }
}
