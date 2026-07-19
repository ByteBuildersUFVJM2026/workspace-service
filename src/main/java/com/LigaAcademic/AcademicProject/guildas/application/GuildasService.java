package com.ligaacademic.academicproject.guildas.application;

import com.ligaacademic.academicproject.guildas.api.GuildasRequestDTO;
import com.ligaacademic.academicproject.guildas.api.GuildasResponseDTO;
import com.ligaacademic.academicproject.guildas.infra.GuildasMapper;
import com.ligaacademic.academicproject.guildas.domain.GuildasModel;
import com.ligaacademic.academicproject.guildas.infra.GuildasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuildasService {

    private final GuildasMapper guildasMapper;
    private final GuildasRepository guildasRepository;

    public GuildasService(GuildasRepository guildasRepository, GuildasMapper guildasMapper) {
        this.guildasRepository = guildasRepository;
        this.guildasMapper = guildasMapper;
    }

    @Transactional(readOnly = true)
    public Page<GuildasResponseDTO> listaTodas(Pageable pageable) {
        return guildasRepository.findAll(pageable)
                .map(guildasMapper::guildaParaResponseDTO);
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

        guildaExistente.setNomeGuilda(dto.nome_guilda());
        guildaExistente.setTutorGuilda(dto.tutor_guilda());
        guildaExistente.setQuantidadePessoas(dto.quantidade_pessoas());

        guildasRepository.save(guildaExistente);
        return guildasMapper.guildaParaResponseDTO(buscarEntidade(id));
    }

    public GuildasResponseDTO atualizarQuantidadePessoas(Long id, int quantidadePessoas) {
        GuildasModel guildaExistente = buscarEntidade(id);

        guildaExistente.setQuantidadePessoas(quantidadePessoas);

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
