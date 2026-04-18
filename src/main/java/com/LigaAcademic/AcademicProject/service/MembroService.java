package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.DTO.MembroUpdateRequestDTO;
import com.LigaAcademic.AcademicProject.User.User;
import com.LigaAcademic.AcademicProject.User.UsersRoles;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.LigaAcademic.AcademicProject.User.UsersRoles.ROLE_USER;

@Service
public class MembroService {


    private MembroRepository membroRepository;
    private GuildasRepository guildasRepository;

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

    public void vincularMembroGuilda(String matricula, Long id){
        Membro membro = membroRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Matricula não encontrada!"
                ));
        GuildasModel guildas = guildasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Guilda com id" + id + "não encontrada."
                ));
        membro.getGuildasModel().add(guildas);

        membroRepository.save(membro);
    }
}


