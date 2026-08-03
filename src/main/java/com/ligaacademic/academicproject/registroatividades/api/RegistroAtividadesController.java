package com.ligaacademic.academicproject.registroatividades.api;

import com.ligaacademic.academicproject.registroatividades.api.RegistroAtividadesRequestDTO;
import com.ligaacademic.academicproject.registroatividades.api.RegistroAtividadesResponseDTO;
import com.ligaacademic.academicproject.shared.auditoria.AuditarAcao;
import com.ligaacademic.academicproject.registroatividades.application.RegistroAtividadesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<RegistroAtividadesResponseDTO>> listarHoras(@PageableDefault(size = 20, sort = "dataAtividade") Pageable pageable) {
        return ResponseEntity.ok(registroAtividadesService.listarTodos(pageable));
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
