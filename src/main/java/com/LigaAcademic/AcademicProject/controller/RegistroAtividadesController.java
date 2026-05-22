package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.DTO.RegistroAtividadesRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.RegistroAtividadesResponseDTO;
import com.LigaAcademic.AcademicProject.Infra.auditoria.AuditarAcao;
import com.LigaAcademic.AcademicProject.Mapper.RegistroAtividadesMapper;
import com.LigaAcademic.AcademicProject.model.RegistroAtividades;
import com.LigaAcademic.AcademicProject.service.RegistroAtividadesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contabilhoras")
public class RegistroAtividadesController {

    private final RegistroAtividadesMapper mapper;
    private final RegistroAtividadesService registroAtividadesService;

    public RegistroAtividadesController(RegistroAtividadesMapper mapper,
                                        RegistroAtividadesService registroAtividadesService) {
        this.mapper = mapper;
        this.registroAtividadesService = registroAtividadesService;
    }

    @PreAuthorize("hasRole('DIRETOR')")
    @PostMapping
    public ResponseEntity<RegistroAtividadesResponseDTO> contabilizarHoras(
            @Validated @RequestBody RegistroAtividadesRequestDTO registroAtividadesRequestDTO) {

        RegistroAtividades horasConvertido = mapper.horasParaEntidade(registroAtividadesRequestDTO);
        RegistroAtividades horasSalvas = registroAtividadesService.registrarHoras(
                horasConvertido, registroAtividadesRequestDTO.matriculas());

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.horasParaResponseDTO(horasSalvas));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<RegistroAtividadesResponseDTO>> listarAtividadesDoParticipante(
            @PathVariable String matricula) {

        List<RegistroAtividades> listaEntidades = registroAtividadesService.listarAtividadesParticipante(matricula);

        List<RegistroAtividadesResponseDTO> respostaDto = listaEntidades.stream()
                .map(mapper::horasParaResponseDTO)
                .toList();

        return ResponseEntity.ok(respostaDto);
    }

    @GetMapping
    public ResponseEntity<List<RegistroAtividadesResponseDTO>> listarHoras() {
        List<RegistroAtividadesResponseDTO> respostaDto = registroAtividadesService.listarTodos().stream()
                .map(mapper::horasParaResponseDTO)
                .toList();

        return ResponseEntity.ok(respostaDto);
    }

    @AuditarAcao(acao = "Delete de registro de horas.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        registroAtividadesService.apagarRegistro(id);
        return ResponseEntity.noContent().build();
    }
}
