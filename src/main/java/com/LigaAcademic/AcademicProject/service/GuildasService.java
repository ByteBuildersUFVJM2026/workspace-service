package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildasService {

    @Autowired
    private GuildasRepository guildasRepository;


    public void registrarGuilda(GuildasModel novaguilda){
       if( novaguilda.getNome_guilda() == null || novaguilda.getNome_guilda().trim().isEmpty()){
           throw new IllegalArgumentException("Adicione o nome da guilda.");
       }
       if(novaguilda.getTutor_guildas() == null || novaguilda.getTutor_guildas().trim().isEmpty()){
           throw new IllegalArgumentException("Adicione o tutor da guilda.");
       }
       guildasRepository.save(novaguilda);
    }

    public void removerGuilda(Long id){
        if(id == null || id == 0){
            throw new IllegalArgumentException("Id não encontrado.");
        }

        GuildasModel guildaParaRemover = guildasRepository.getById(id);

        if(guildaParaRemover != null){
            guildasRepository.delete(guildaParaRemover);
        }else{
            throw new IllegalArgumentException("Guilda não encontrada");
        }

    }

}
