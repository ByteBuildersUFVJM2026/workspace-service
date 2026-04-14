package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import com.LigaAcademic.AcademicProject.repository.ContabilHorasRepository;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ContabilHorasService {

    @Autowired
    private ContabilHorasRepository contabilHorasRepository;

    public ContabilHoras registrarHoras(ContabilHoras contabilHoras){

        return contabilHorasRepository.save(contabilHoras);
    }

    public List<ContabilHoras> listarTodos(){
        return contabilHorasRepository.findAll();
    }

    public void apagarRegistro(Long idParaRemover){
        if(idParaRemover == null){
            throw new IllegalArgumentException("O id não pode ser vazio.");
        }

        ContabilHoras IdEncontrado = contabilHorasRepository.findById(idParaRemover).orElseThrow(() -> new RuntimeException("Não encontrado"));

        if(IdEncontrado != null){
            contabilHorasRepository.delete(IdEncontrado);
        }else{
            throw new IllegalArgumentException("Contabilização com esse ID não encontrada");
        }


    }



}
