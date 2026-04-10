
package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.User.MembroRequestDTO;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.MembroMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String adicionarMembro(@RequestBody @Validated MembroRequestDTO membroRequestDTO){

        Membro membroConvertido = mapper.paraEntidade(membroRequestDTO);

        membroService.registrarMembro(membroConvertido);

        return "Requisição recebida e membro registrado";
    }

    @GetMapping("/{matricula}")
    public String buscar(@PathVariable String matricula){
        membroService.listarMembro(matricula);
        return "Matricula buscada com sucesso";

    }

    @PutMapping("/{matricula}")
    public String atualizar(@PathVariable String matricula, @RequestBody Membro membroAtualizado){
        membroService.atualizarMembro(
                membroAtualizado.getNome(),
                matricula,
                membroAtualizado.getEmail(),
                membroAtualizado.getCargo()
        );
        return "Requisição PUT recebida!";
    }

    @DeleteMapping("/{matricula}")
    public String deletar(@PathVariable String matricula){
        membroService.removerMembro(matricula);
        return "Membro deletado";
    }
}
