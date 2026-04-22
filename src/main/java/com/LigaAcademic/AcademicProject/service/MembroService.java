package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.DTO.MembroUpdateRequestDTO;
import com.LigaAcademic.AcademicProject.Infra.Exceptions.ConflictException;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroService {


    private final MembroRepository membroRepository;
    private final GuildasRepository guildasRepository;

    public MembroService(MembroRepository membroRepository, GuildasRepository guildasRepository) {
        this.membroRepository = membroRepository;
        this.guildasRepository = guildasRepository;
    }

    public Membro registrarMembro(Membro membronovo) {

        return membroRepository.save(membronovo);
    }

    public void removerMembro(String matriculaRemove) {
        if (matriculaRemove == null || matriculaRemove.trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula é inválida");
        }

        Membro membroEncontrado = membroRepository.findByMatricula(matriculaRemove)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Não é possível deletar: membro com matrícula " + matriculaRemove + " não existe"
                ));

        membroRepository.delete(membroEncontrado);
    }

    public Membro atualizarMembro(String matricula, MembroUpdateRequestDTO dto) {

        Membro membroExistente = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Erro ao atualizar: matrícula " + matricula + " não encontrada"
                ));

        membroExistente.setNome(dto.nome());
        membroExistente.setEmail(dto.email());

        return membroRepository.save(membroExistente);
    }

    public Membro buscarMembro(String matricula){

        return membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException("Membro não encontrado para a matrícula " + matricula));
    }

    public List<Membro> listaTodos(){
        return membroRepository.buscarTodosComGuildas();
    }

    public void vincularMembroGuilda(String matricula, Long id) {
        Membro membro = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Membro com matrícula " + matricula + " não encontrado"
                ));
        GuildasModel guildas = guildasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Guilda com id " + id + " não encontrada"
                ));

        boolean jaVinculado = membro.getGuildasModel().stream()
                .anyMatch(g -> g.getId().equals(id));
        if (jaVinculado) {
            throw new ConflictException(
                    "Membro " + matricula + " já pertence à guilda " + id
            );
        }

        membro.getGuildasModel().add(guildas);
        membroRepository.save(membro);
    }

    public void desvincularMembroGuilda(String matricula, Long id) {

        Membro membro = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException("Membro não encontrado"));

        boolean removido = membro.getGuildasModel().removeIf(g -> g.getId().equals(id));
        if (!removido) {
            throw new EntityNotFoundException(
                    "Membro " + matricula + " não pertence à guilda " + id
            );
        }

        membroRepository.save(membro);
    }


}


