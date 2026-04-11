package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.model.Membro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;

import java.util.List;

@Service
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    public void registrarMembro(Membro membronovo) {

        membroRepository.save(membronovo);
    }

    public void removerMembro(String matriculaRemove){
        if(matriculaRemove == null || matriculaRemove.trim().isEmpty()){
            throw new IllegalArgumentException("Matricula é inválida");
        }

            Membro membroEncontrado = membroRepository.findByMatricula(matriculaRemove);

        if(membroEncontrado != null){
            System.out.println("Membro encontrado");
            membroRepository.delete(membroEncontrado);
        }else{
            throw new IllegalArgumentException("Membro não encontrado");
        }

    }

    public void atualizarMembro(String nome,String matricula, String email, String cargo){

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (matricula == null || matricula.length() < 11 || matricula.length() > 11 || !matricula.matches("[0-9]+")) {
            throw new IllegalArgumentException("Adicione o formato correto de matricula");
        }

        if (email == null || !email.contains("@") || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Coloque um email válido");
        }
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("Cargo do membro não pode ser vazio");
        }
    }

    public void listarMembro(String matricula){
        if (matricula == null || matricula.length() < 11 || matricula.length() > 11 || !matricula.matches("[0-9]+")) {
            throw new IllegalArgumentException("O número de mátricula precisa ter 11 dígitos ou não existe");
        }
        Membro membroMatricula = membroRepository.findByMatricula(matricula);
    }

    public List<Membro> listaTodos(){
        return membroRepository.findAll();
    }
}
