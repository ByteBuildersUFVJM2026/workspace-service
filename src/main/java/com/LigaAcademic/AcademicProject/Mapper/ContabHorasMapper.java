package com.LigaAcademic.AcademicProject.Mapper;


import com.LigaAcademic.AcademicProject.DTO.ContabHorasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.ContabHorasResponseDTO;
import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContabHorasMapper {

    ContabilHoras horasParaEntidade(ContabHorasRequestDTO contabHorasRequestDTO);

    ContabHorasResponseDTO horasParaResponseDTO(ContabilHoras contabilHoras);
}
