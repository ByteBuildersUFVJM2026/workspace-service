package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import com.LigaAcademic.AcademicProject.repository.ContabilHorasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContabilHorasService {


    private ContabilHorasRepository contabilHorasRepository;

    public ContabilHorasService(ContabilHorasRepository contabilHorasRepository) {
        this.contabilHorasRepository = contabilHorasRepository;
    }

    public ContabilHoras registrarHoras(ContabilHoras contabilHoras){

        return contabilHorasRepository.save(contabilHoras);
    }

    public List<ContabilHoras> listarAtividadesParticipante(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula inválida");
        }

        List<ContabilHoras> listaDeAtividades = contabilHorasRepository.findByMatricula(matricula);

        if (listaDeAtividades.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma atividade encontrada para a matrícula " + matricula);
        }

        return listaDeAtividades;
    }

    public List<ContabilHoras> listarTodos(){
        return contabilHorasRepository.findAll();
    }

    public void apagarRegistro(Long idParaRemover) {
        ContabilHoras registro = contabilHorasRepository.findById(idParaRemover)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + idParaRemover + " não encontrado"
                ));

        contabilHorasRepository.delete(registro);
    }



}
