package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuildasService {

    private GuildasRepository guildasRepository;

    public GuildasService(GuildasRepository guildasRepository) {
        this.guildasRepository = guildasRepository;
    }

    public List<GuildasModel> listaTodas() {

        return guildasRepository.findAll();
    }

    public GuildasModel buscarGuilda(Long id) {

        return guildasRepository.findById(id)

                .orElseThrow(() -> new IllegalArgumentException("Guilda não encontrada para o id: " + id));
    }

    public GuildasModel registrarGuilda(GuildasModel novaguilda) {


        return guildasRepository.save(novaguilda);
    }

    public GuildasModel atualizarGuilda(Long id, GuildasModel dadosAtualizados) {
        GuildasModel guildaExistente = buscarGuilda(id);

        guildaExistente.setNome_guilda(dadosAtualizados.getNome_guilda());

        guildaExistente.setTutor_guilda(dadosAtualizados.getTutor_guilda());

        guildaExistente.setQuantidade_pessoas(dadosAtualizados.getQuantidade_pessoas());

        return guildasRepository.save(guildaExistente);
    }

    public GuildasModel atualizarQuantidadePessoas(Long id, int quantidadePessoas) {
        GuildasModel guildaExistente = buscarGuilda(id);

        guildaExistente.setQuantidade_pessoas(quantidadePessoas);

        return guildasRepository.save(guildaExistente);
    }

    public void removerGuilda(Long id) {

        GuildasModel guildaParaRemover = buscarGuilda(id);

        guildasRepository.delete(guildaParaRemover);
    }
}