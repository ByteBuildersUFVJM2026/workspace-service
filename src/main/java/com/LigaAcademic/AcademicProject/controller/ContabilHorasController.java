package com.LigaAcademic.AcademicProject.controller;


import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.service.ContabilHorasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/contabilhoras")
public class ContabilHorasController {


    @Autowired
    private ContabilHorasService contabilHorasService;


    @PostMapping
    public String contabilizarHoras(@RequestBody ContabilHoras contabilHoras){
        contabilHorasService.registrarHoras(contabilHoras);
        return "Horas contabilizada";
    }

    @GetMapping
    public List<ContabilHoras> listarHoras(){
        return contabilHorasService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        contabilHorasService.apagarRegistro(id);
        return "Membro deletado";
    }

}
