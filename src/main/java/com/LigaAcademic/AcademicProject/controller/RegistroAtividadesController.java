package com.ligaacademic.academicproject.controller;

import com.ligaacademic.academicproject.dto.RegistroAtividadesRequestDTO;
import com.ligaacademic.academicproject.dto.RegistroAtividadesResponseDTO;
import com.ligaacademic.academicproject.infra.auditoria.AuditarAcao;
import com.ligaacademic.academicproject.service.RegistroAtividadesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contabilhoras")
public class RegistroAtividadesController {

    private final RegistroAtividadesService registroAtividadesService;

    public RegistroAtividadesController(RegistroAtividadesService registroAtividadesService) {
        this.registroAtividadesService = registroAtividadesService;
    }

    @PreAuthorize("hasRole('DIRETOR')")
    @PostMapping
    public ResponseEntity<RegistroAtividadesResponseDTO> contabilizarHoras(
            @Validated @RequestBody RegistroAtividadesRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroAtividadesService.registrarHoras(dto));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<RegistroAtividadesResponseDTO>> listarAtividadesDoParticipante(
            @PathVariable String matricula) {
        return ResponseEntity.ok(registroAtividadesService.listarAtividadesParticipante(matricula));
    }

    @GetMapping
    public ResponseEntity<List<RegistroAtividadesResponseDTO>> listarHoras() {
        return ResponseEntity.ok(registroAtividadesService.listarTodos());
    }

    @PreAuthorize("hasRole('DIRETOR')")
    @PutMapping("/{id}")
    public ResponseEntity<RegistroAtividadesResponseDTO> atualizar(
            @PathVariable Long id,
            @Validated @RequestBody RegistroAtividadesRequestDTO dto) {
        return ResponseEntity.ok(registroAtividadesService.atualizarRegistroAtividades(id, dto));
    }

    @AuditarAcao(acao = "Delete de registro de horas.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        registroAtividadesService.apagarRegistro(id);
        return ResponseEntity.noContent().build();
    }
}
