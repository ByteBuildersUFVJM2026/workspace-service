package com.LigaAcademic.AcademicProject.Mapper;

import com.LigaAcademic.AcademicProject.DTO.GuildasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasResponseDTO;
import com.LigaAcademic.AcademicProject.model.GuildasModel;

public interface GuildasMapper {

    GuildasModel guildaParaEntidade(GuildasRequestDTO guildasRequestDTO);

    GuildasResponseDTO guildaParaResponseDTO(GuildasModel guildasModel);
}
