package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.Mapper.GuildasMapper;
import com.LigaAcademic.AcademicProject.DTO.GuildasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasResponseDTO;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.service.GuildasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public class GuildasController {

    private GuildasMapper guildasMapper;
    private GuildasService guildasService;

    public GuildasController(GuildasMapper guildasMapper, GuildasService guildasService) {
        this.guildasMapper = guildasMapper;
        this.guildasService = guildasService;
    }

    public ResponseEntity<GuildasResponseDTO> registroDeGuilda(@Validated @RequestBody GuildasRequestDTO guildasRequestDTO){

            GuildasModel novaGuilda = guildasMapper.guildaParaEntidade(guildasRequestDTO);

            GuildasModel salvarNovaGuilda = guildasService.registrarGuilda(novaGuilda);

            return ResponseEntity.status(HttpStatus.CREATED).body(guildasMapper.guildaParaResponseDTO(salvarNovaGuilda));

        }
}
