package com.ligaacademic.academicproject.guildas.api;

import com.ligaacademic.academicproject.guildas.api.GuildasQuantidadePessoasRequestDTO;
import com.ligaacademic.academicproject.guildas.api.GuildasRequestDTO;
import com.ligaacademic.academicproject.guildas.api.GuildasResponseDTO;
import com.ligaacademic.academicproject.shared.auditoria.AuditarAcao;
import com.ligaacademic.academicproject.guildas.application.GuildasService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guildas")
public class GuildasController {

    private final GuildasService guildasService;

    public GuildasController(GuildasService guildasService) {
        this.guildasService = guildasService;
    }

    @GetMapping
    public ResponseEntity<Page<GuildasResponseDTO>> listarTodas(@PageableDefault(size = 20, sort = "nomeGuilda") Pageable pageable) {
        return ResponseEntity.ok(guildasService.listaTodas(pageable));
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
