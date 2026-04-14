package com.LigaAcademic.AcademicProject.controller;


import com.LigaAcademic.AcademicProject.User.ContabHorasRequestDTO;
import com.LigaAcademic.AcademicProject.User.ContabHorasResponseDTO;
import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import com.LigaAcademic.AcademicProject.Mapper.ContabHorasMapper;
import com.LigaAcademic.AcademicProject.service.ContabilHorasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/contabilhoras")
public class ContabilHorasController {

    @Autowired
    private ContabHorasMapper mapper;

    @Autowired
    private ContabilHorasService contabilHorasService;


    @PostMapping
    public ResponseEntity<ContabHorasResponseDTO> contabilizarHoras(@Validated @RequestBody ContabHorasRequestDTO contabHorasRequestDTO){

        ContabilHoras horasConvertido = mapper.horasParaEntidade(contabHorasRequestDTO);

        ContabilHoras horasSalvas = contabilHorasService.registrarHoras(horasConvertido);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.horasParaResponseDTO(horasSalvas));
    }

    @GetMapping
    public List<ContabilHoras> listarHoras(){
        return contabilHorasService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        contabilHorasService.apagarRegistro(id);
        return "Membro deletado";
    }

}
