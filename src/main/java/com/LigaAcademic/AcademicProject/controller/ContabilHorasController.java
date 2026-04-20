package com.LigaAcademic.AcademicProject.controller;


import com.LigaAcademic.AcademicProject.DTO.ContabHorasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.ContabHorasResponseDTO;
import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import com.LigaAcademic.AcademicProject.Mapper.ContabHorasMapper;
import com.LigaAcademic.AcademicProject.service.ContabilHorasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/contabilhoras")
public class ContabilHorasController {


    private ContabHorasMapper mapper;
    private ContabilHorasService contabilHorasService;


    public ContabilHorasController(ContabHorasMapper mapper, ContabilHorasService contabilHorasService) {
        this.mapper = mapper;
        this.contabilHorasService = contabilHorasService;
    }

    @PostMapping
    public ResponseEntity<ContabHorasResponseDTO> contabilizarHoras(@Validated @RequestBody ContabHorasRequestDTO contabHorasRequestDTO){

        ContabilHoras horasConvertido = mapper.horasParaEntidade(contabHorasRequestDTO);

        ContabilHoras horasSalvas = contabilHorasService.registrarHoras(horasConvertido);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.horasParaResponseDTO(horasSalvas));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<ContabHorasResponseDTO>> listarAtividadesDoParticipante(@PathVariable String participante){

        List<ContabilHoras> listaEntidades = contabilHorasService.listarAtividadesParticipante(participante);

        List<ContabHorasResponseDTO> respostaDto = listaEntidades.stream()
                .map(entidade -> mapper.horasParaResponseDTO(entidade))
                .toList();

        return ResponseEntity.ok(respostaDto);
    }

    @GetMapping
    public ResponseEntity<List<ContabHorasResponseDTO>> listarHoras(){
        List<ContabHorasResponseDTO> respostaDto = contabilHorasService.listarTodos().stream()
                .map(mapper::horasParaResponseDTO)
                .toList();

        return ResponseEntity.ok(respostaDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        contabilHorasService.apagarRegistro(id);
        return ResponseEntity.noContent().build();
    }

}
