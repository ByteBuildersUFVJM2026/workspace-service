package com.LigaAcademic.AcademicProject.Mapper;


import com.LigaAcademic.AcademicProject.User.ContabHorasRequestDTO;
import com.LigaAcademic.AcademicProject.User.ContabHorasResponseDTO;
import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContabHorasMapper {

    ContabilHoras horasParaEntidade(ContabHorasRequestDTO contabHorasRequestDTO);

    ContabHorasResponseDTO horasParaResponseDTO(ContabilHoras contabilHoras);
}
