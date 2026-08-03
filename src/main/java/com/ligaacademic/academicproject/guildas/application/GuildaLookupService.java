package com.ligaacademic.academicproject.guildas.application;

import com.ligaacademic.academicproject.guildas.domain.GuildasModel;
import com.ligaacademic.academicproject.guildas.infra.GuildasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GuildaLookupService {

    private final GuildasRepository guildasRepository;

    public GuildaLookupService(GuildasRepository guildasRepository) {
        this.guildasRepository = guildasRepository;
    }

    public GuildasModel buscarPorId(Long id) {
        return guildasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guilda com id " + id + " nao encontrada"));
    }
}
