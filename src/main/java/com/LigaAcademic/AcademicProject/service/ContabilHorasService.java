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

    public void registrarHoras(ContabilHoras contabilHoras){

            if (contabilHoras.getParticipantes() == null || contabilHoras.getParticipantes().trim().isEmpty()) {
                throw new IllegalArgumentException("Adicione ao menos 1 participante");
            }
            if (contabilHoras.getTipoAtividade() == null || contabilHoras.getTipoAtividade().trim().isEmpty()) {
                throw new IllegalArgumentException("Adicione o tipo de atividade");
            }
            if (contabilHoras.getDescAtividade() == null || contabilHoras.getDescAtividade().trim().isEmpty()) {
                throw new IllegalArgumentException("Adicione a descrição da atividade");
            }
            if(contabilHoras.getDataAtividade() == null){
                throw new IllegalArgumentException("A data não pode ser nula");
            }
            try {
                contabilHorasRepository.save(contabilHoras);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Erro de validação: A data" + contabilHoras.getDataAtividade() + "é inválida. Utilize exatamente o formato: AAAA-MM-DD");
            }
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
