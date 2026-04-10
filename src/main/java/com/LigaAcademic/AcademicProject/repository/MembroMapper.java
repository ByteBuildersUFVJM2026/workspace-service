package com.LigaAcademic.AcademicProject.repository;

import com.LigaAcademic.AcademicProject.User.MembroRequestDTO;
import com.LigaAcademic.AcademicProject.User.MembroResponseDTO;
import com.LigaAcademic.AcademicProject.model.Membro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembroMapper {

    Membro paraEntidade(MembroRequestDTO dto);

    MembroResponseDTO paraDTO(Membro membro);


}
