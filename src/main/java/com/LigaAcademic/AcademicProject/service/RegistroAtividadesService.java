package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.model.RegistroAtividades;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;
import com.LigaAcademic.AcademicProject.repository.RegistroAtividadesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroAtividadesService {

    private final RegistroAtividadesRepository registroAtividadesRepository;
    private final MembroRepository membroRepository;

    public RegistroAtividadesService(RegistroAtividadesRepository registroAtividadesRepository,
                                     MembroRepository membroRepository) {
        this.registroAtividadesRepository = registroAtividadesRepository;
        this.membroRepository = membroRepository;
    }

    @Transactional
    public RegistroAtividades registrarHoras(RegistroAtividades registroAtividades, List<String> matriculas) {
        List<Membro> membros = matriculas.stream()
                .map(matricula -> membroRepository.findByMatricula(matricula)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Membro com matrícula " + matricula + " não encontrado")))
                .toList();

        membros.forEach(membro -> membro.setTotalHoras(membro.getTotalHoras() + registroAtividades.getHoras()));
        registroAtividades.setParticipantes(membros);

        membroRepository.saveAll(membros);
        RegistroAtividades salvo = registroAtividadesRepository.save(registroAtividades);
        return registroAtividadesRepository.findByIdComParticipantes(salvo.getId()).orElseThrow();
    }

    @Transactional
    public List<RegistroAtividades> listarAtividadesParticipante(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula inválida");
        }

        List<RegistroAtividades> listaDeAtividades = registroAtividadesRepository.findByParticipanteMatricula(matricula);

        if (listaDeAtividades.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma atividade encontrada para a matrícula " + matricula);
        }

        return listaDeAtividades;
    }

    @Transactional
    public List<RegistroAtividades> listarTodos() {
        return registroAtividadesRepository.findAllComParticipantes();
    }

    @Transactional
    public void apagarRegistro(Long idParaRemover) {
        RegistroAtividades registro = registroAtividadesRepository.findByIdComParticipantes(idParaRemover)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + idParaRemover + " não encontrado"));

        registro.getParticipantes().forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras() - registro.getHoras()));

        membroRepository.saveAll(registro.getParticipantes());
        registroAtividadesRepository.delete(registro);
    }
}
