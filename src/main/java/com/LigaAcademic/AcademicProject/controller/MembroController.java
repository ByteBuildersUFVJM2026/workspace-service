package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.DTO.MembroRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.MembroResponseDTO;
import com.LigaAcademic.AcademicProject.DTO.MembroUpdateRequestDTO;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.Mapper.MembroMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.LigaAcademic.AcademicProject.service.MembroService;

import java.util.List;

@RestController
@RequestMapping("/membros")
public class MembroController {


    private MembroMapper mapper;
    private MembroService membroService;


    public MembroController(MembroMapper mapper, MembroService membroService) {
        this.mapper = mapper;
        this.membroService = membroService;
    }

    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> listarTodos() {
        List<MembroResponseDTO> membros = membroService.listaTodos().stream()
                .map(mapper::paraResponseDTO)
                .toList();
        return ResponseEntity.ok(membros);
    }


    @PostMapping
    public ResponseEntity<MembroResponseDTO> adicionarMembro(@RequestBody @Validated MembroRequestDTO membroRequestDTO){

        Membro membroConvertido = mapper.paraEntidade(membroRequestDTO);

        Membro membroSalvo = membroService.registrarMembro(membroConvertido);


        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.paraResponseDTO(membroSalvo));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<MembroResponseDTO> buscar(@PathVariable String matricula){

        Membro membroEncontrado = membroService.buscarMembro(matricula);

        MembroResponseDTO respostaDTO = mapper.paraResponseDTO(membroEncontrado);

        return ResponseEntity.ok(respostaDTO);

    }

    @PatchMapping("/{matricula}")
    public ResponseEntity<MembroResponseDTO> atualizarMembro(
            @PathVariable String matricula,
            @Validated @RequestBody MembroUpdateRequestDTO membroUpdateRequestDTO) {

        Membro membroSalvo = membroService.atualizarMembro(matricula, membroUpdateRequestDTO);

        return ResponseEntity.ok(mapper.paraResponseDTO(membroSalvo));
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletar(@PathVariable String matricula){

        membroService.removerMembro(matricula);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{matricula}/guildas/{idGuilda}")
    public ResponseEntity<Void> vincularGuilda(@PathVariable String matricula, @PathVariable Long idGuilda) {

        membroService.vincularMembroGuilda(matricula, idGuilda);

        return ResponseEntity.noContent().build();
    }
}
