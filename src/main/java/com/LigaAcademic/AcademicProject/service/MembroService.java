package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.DTO.MembroUpdateRequestDTO;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void removerMembro(String matriculaRemove){
        if(matriculaRemove == null || matriculaRemove.trim().isEmpty()){
            throw new IllegalArgumentException("Matricula é inválida");
        }

            Membro membroEncontrado = membroRepository.findByMatricula(matriculaRemove)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,"Não é possível deletar: Membro com matricula" + matriculaRemove + " não existe"
                    ));

        if(membroEncontrado != null){
            System.out.println("Membro encontrado");
            membroRepository.delete(membroEncontrado);
        }else{
            throw new IllegalArgumentException("Membro não encontrado");
        }

    }

    public Membro atualizarMembro(String matricula, MembroUpdateRequestDTO dto) {

        Membro membroExistente = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Erro ao atualizar: Matrícula " + matricula + " não encontrada"
                ));

        membroExistente.setNome(dto.nome());
        membroExistente.setEmail(dto.email());

        return membroRepository.save(membroExistente);
    }

    public Membro buscarMembro(String matricula){

        return membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Matricula não encontrada!"
                ));
    }

    public List<Membro> listaTodos(){
        return membroRepository.buscarTodosComGuildas();
    }

    public void vincularMembroGuilda(String matricula, Long id) {
        Membro membro = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Matricula não encontrada!"
                ));
        GuildasModel guildas = guildasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Guilda com id " + id + " não encontrada."
                ));

        boolean jaVinculado = membro.getGuildasModel().stream()
                .anyMatch(g -> g.getId().equals(id));
        if (jaVinculado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Membro " + matricula + " já pertence à guilda " + id
            );
        }

        membro.getGuildasModel().add(guildas);
        membroRepository.save(membro);
    }

    public void desvincularMembroGuilda(String matricula, Long id) {
        Membro membro = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Matricula não encontrada!"
                ));

        boolean removido = membro.getGuildasModel().removeIf(g -> g.getId().equals(id));
        if (!removido) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Membro " + matricula + " não pertence à guilda " + id
            );
        }

        membroRepository.save(membro);
    }


}


