package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.DTO.GuildasQuantidadePessoasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasResponseDTO;
import com.LigaAcademic.AcademicProject.Infra.auditoria.AuditarAcao;
import com.LigaAcademic.AcademicProject.service.GuildasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guildas")
public class GuildasController {

    private final GuildasService guildasService;

    public GuildasController(GuildasService guildasService) {
        this.guildasService = guildasService;
    }

    @GetMapping
    public ResponseEntity<List<GuildasResponseDTO>> listarTodas() {
        return ResponseEntity.ok(guildasService.listaTodas());
    }

    @PreAuthorize("hasRole('DIRETOR')")
    @PostMapping
    public ResponseEntity<GuildasResponseDTO> registroDeGuilda(@RequestBody @Validated GuildasRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guildasService.registrarGuilda(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuildasResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(guildasService.buscarGuilda(id));
    }

    @PreAuthorize("hasRole('DIRETOR')")
    @PutMapping("/{id}")
    public ResponseEntity<GuildasResponseDTO> atualizarGuilda(@PathVariable Long id, @Validated @RequestBody GuildasRequestDTO dto) {
        return ResponseEntity.ok(guildasService.atualizarGuilda(id, dto));
    }

    @AuditarAcao(acao = "Atualização da quantidade de pessoas na guilda.")
    @PreAuthorize("hasRole('DIRETOR')")
    @PatchMapping("/{id}/quantidade-pessoas")
    public ResponseEntity<GuildasResponseDTO> atualizarQuantidadePessoas(@PathVariable Long id, @Validated @RequestBody GuildasQuantidadePessoasRequestDTO dto) {
        return ResponseEntity.ok(guildasService.atualizarQuantidadePessoas(id, dto.quantidade_pessoas()));
    }

    @AuditarAcao(acao = "Delete de guilda.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        guildasService.removerGuilda(id);
        return ResponseEntity.noContent().build();
    }
}