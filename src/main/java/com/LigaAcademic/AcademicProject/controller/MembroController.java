
package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.User.MembroRequestDTO;
import com.LigaAcademic.AcademicProject.User.MembroResponseDTO;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.MembroMapper;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.LigaAcademic.AcademicProject.service.MembroService;

import java.util.List;

@RestController
@RequestMapping("/membros")
public class MembroController {

    @Autowired
    private MembroMapper mapper;

    @Autowired
    private MembroService membroService;

    @GetMapping
    public ResponseEntity<List<Membro>>listarTodos(){
        return ResponseEntity.ok(membroService.listaTodos());
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

    @PutMapping("/{matricula}")
    public ResponseEntity<MembroResponseDTO> atualizarMembro(@PathVariable String matricula, @Validated @RequestBody MembroRequestDTO membroRequestDTO){

        Membro membroConvertido = mapper.paraEntidade(membroRequestDTO);

        Membro membroSalvo = membroService.atualizarMembro(matricula, membroConvertido);

        return ResponseEntity.ok(mapper.paraResponseDTO(membroSalvo));
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletar(@PathVariable String matricula){

        membroService.removerMembro(matricula);

        return ResponseEntity.noContent().build();
    }
}
