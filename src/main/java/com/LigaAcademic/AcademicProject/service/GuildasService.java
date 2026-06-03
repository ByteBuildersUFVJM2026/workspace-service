package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.DTO.GuildasRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.GuildasResponseDTO;
import com.LigaAcademic.AcademicProject.Mapper.GuildasMapper;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuildasService {

    private final GuildasMapper guildasMapper;
    private final GuildasRepository guildasRepository;

    public GuildasService(GuildasRepository guildasRepository, GuildasMapper guildasMapper) {
        this.guildasRepository = guildasRepository;
        this.guildasMapper = guildasMapper;
    }

    public List<GuildasResponseDTO> listaTodas() {
        return guildasRepository.findAllComMembros()
                .stream()
                .map(guildasMapper::guildaParaResponseDTO)
                .toList();
    }

    public GuildasResponseDTO buscarGuilda(Long id) {
        return guildasMapper.guildaParaResponseDTO(buscarEntidade(id));
    }

    public GuildasResponseDTO registrarGuilda(GuildasRequestDTO dto) {
        GuildasModel salva = guildasRepository.save(guildasMapper.guildaParaEntidade(dto));
        return guildasMapper.guildaParaResponseDTO(buscarEntidade(salva.getId()));
    }

    public GuildasResponseDTO atualizarGuilda(Long id, GuildasRequestDTO dto) {
        GuildasModel guildaExistente = buscarEntidade(id);

        guildaExistente.setNome_guilda(dto.nome_guilda());
        guildaExistente.setTutor_guilda(dto.tutor_guilda());
        guildaExistente.setQuantidade_pessoas(dto.quantidade_pessoas());

        guildasRepository.save(guildaExistente);
        return guildasMapper.guildaParaResponseDTO(buscarEntidade(id));
    }

    public GuildasResponseDTO atualizarQuantidadePessoas(Long id, int quantidadePessoas) {
        GuildasModel guildaExistente = buscarEntidade(id);

        guildaExistente.setQuantidade_pessoas(quantidadePessoas);

        guildasRepository.save(guildaExistente);
        return guildasMapper.guildaParaResponseDTO(buscarEntidade(id));
    }

    public void removerGuilda(Long id) {
        guildasRepository.delete(buscarEntidade(id));
    }

    private GuildasModel buscarEntidade(Long id) {
        return guildasRepository.findByIdComMembros(id)
                .orElseThrow(() -> new EntityNotFoundException("Guilda não encontrada para o id: " + id));
    }
}