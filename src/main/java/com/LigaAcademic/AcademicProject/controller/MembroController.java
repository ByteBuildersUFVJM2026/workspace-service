package com.ligaacademic.academicproject.controller;

import com.ligaacademic.academicproject.dto.MembroRequestDTO;
import com.ligaacademic.academicproject.dto.MembroResponseDTO;
import com.ligaacademic.academicproject.dto.MembroUpdateRequestDTO;
import com.ligaacademic.academicproject.infra.auditoria.AuditarAcao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ligaacademic.academicproject.service.MembroService;

@RestController
@RequestMapping("/membros")
public class MembroController {



    private MembroService membroService;


    public MembroController(MembroService membroService) {

        this.membroService = membroService;
    }

    @GetMapping
    public ResponseEntity<Page<MembroResponseDTO>> listarTodos(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(membroService.listarTodos(pageable));
    }


    @PreAuthorize("hasRole('DIRETOR')")
    @PostMapping
    public ResponseEntity<MembroResponseDTO> adicionarMembro(@RequestBody @Validated MembroRequestDTO membroRequestDTO){

        return ResponseEntity.status(HttpStatus.CREATED).body(membroService.registrarMembro(membroRequestDTO));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<MembroResponseDTO> buscar(@PathVariable String matricula){

        return ResponseEntity.ok(membroService.buscarMembro(matricula));

    }

    @PreAuthorize("hasRole('DIRETOR')")
    @PutMapping("/{matricula}")//Lembrar de mudar no postman para put para testar
    public ResponseEntity<MembroResponseDTO> atualizarMembro(
            @PathVariable String matricula,
            @Validated @RequestBody MembroUpdateRequestDTO membroUpdateRequestDTO) {

        return ResponseEntity.ok(membroService.atualizarMembro(matricula,membroUpdateRequestDTO));

    }

    @AuditarAcao(acao = "Delete de membro da liga.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletar(@PathVariable String matricula){

        membroService.removerMembro(matricula);

        return ResponseEntity.noContent().build();
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{matricula}/guildas/{idGuilda}")
    public ResponseEntity<Void> vincularGuilda(@PathVariable String matricula, @PathVariable Long idGuilda) {

        membroService.vincularMembroGuilda(matricula, idGuilda);

        return ResponseEntity.noContent().build();

    }

    @AuditarAcao(acao = "Desvinculando membro da guilda.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{matricula}/guilda/{idGuilda}")
    public ResponseEntity<Void> desvincularMembroGuilda(@PathVariable String matricula, @PathVariable Long idGuilda) {
        membroService.desvincularMembroGuilda(matricula, idGuilda);
        return ResponseEntity.noContent().build();
    }
}

