package com.LigaAcademic.AcademicProject.DTO;

import com.LigaAcademic.AcademicProject.model.GuildasModel;
import java.util.List;

public record MembroResponseDTO(
        Long id,
        String nome,
        String matricula,
        String cargo,
        String email,
        List<GuildasModel> guildasModel
) {
}
