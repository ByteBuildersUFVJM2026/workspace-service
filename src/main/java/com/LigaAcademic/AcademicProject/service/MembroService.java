package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.User.User;
import com.LigaAcademic.AcademicProject.User.UsersRoles;
import com.LigaAcademic.AcademicProject.model.Membro;
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

    public MembroService(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
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

    public Membro atualizarMembro(String matricula, Membro membroAtualizado){

        Membro membroExistente = membroRepository.findByMatricula(matricula)

                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Erro ao atualizar> Matrícula " + matricula + "não encontrada"
        ));

        membroExistente.setNome(membroAtualizado.getNome());
        membroExistente.setEmail(membroAtualizado.getEmail());

        return membroRepository.save(membroExistente);


    }

    public Membro buscarMembro(String matricula){

        return membroRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Matricula não encontrada!"
                ));
    }

    public List<Membro> listaTodos(){
        return membroRepository.findAll();
    }
}
