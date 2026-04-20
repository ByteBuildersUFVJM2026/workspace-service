package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.DTO.GuildasQuantidadePessoasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasResponseDTO;
import com.LigaAcademic.AcademicProject.Mapper.GuildasMapper;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.service.GuildasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guildas")
public class GuildasController {

    private GuildasMapper guildasMapper;
    private GuildasService guildasService;

    public GuildasController(GuildasMapper guildasMapper, GuildasService guildasService) {
        this.guildasMapper = guildasMapper;
        this.guildasService = guildasService;
    }

    @GetMapping
    public ResponseEntity<List<GuildasResponseDTO>> listarTodas() {
        List<GuildasResponseDTO> guildas = guildasService.listaTodas().stream()
                .map(guildasMapper::guildaParaResponseDTO)
                .toList();

        return ResponseEntity.ok(guildas);
    }

    @PostMapping
    public ResponseEntity<GuildasResponseDTO> registroDeGuilda(@RequestBody @Validated GuildasRequestDTO guildasRequestDTO) {
        GuildasModel novaGuilda = guildasMapper.guildaParaEntidade(guildasRequestDTO);
        GuildasModel salvarNovaGuilda = guildasService.registrarGuilda(novaGuilda);

        return ResponseEntity.status(HttpStatus.CREATED).body(guildasMapper.guildaParaResponseDTO(salvarNovaGuilda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuildasResponseDTO> buscar(@PathVariable Long id) {
        GuildasModel guildaEncontrada = guildasService.buscarGuilda(id);

        return ResponseEntity.ok(guildasMapper.guildaParaResponseDTO(guildaEncontrada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuildasResponseDTO> atualizarGuilda(@PathVariable Long id, @Validated @RequestBody GuildasRequestDTO guildasRequestDTO) {
        GuildasModel guildaConvertida = guildasMapper.guildaParaEntidade(guildasRequestDTO);
        GuildasModel guildaSalva = guildasService.atualizarGuilda(id, guildaConvertida);

        return ResponseEntity.ok(guildasMapper.guildaParaResponseDTO(guildaSalva));
    }

    @PatchMapping("/{id}/quantidade-pessoas")
    public ResponseEntity<GuildasResponseDTO> atualizarQuantidadePessoas(@PathVariable Long id, @Validated @RequestBody GuildasQuantidadePessoasRequestDTO dto) {
        GuildasModel guildaAtualizada = guildasService.atualizarQuantidadePessoas(id, dto.quantidade_pessoas());

        return ResponseEntity.ok(guildasMapper.guildaParaResponseDTO(guildaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        guildasService.removerGuilda(id);

        return ResponseEntity.noContent().build();
    }
}