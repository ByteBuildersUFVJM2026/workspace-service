package com.ligaacademic.academicproject.membros.application;

import com.ligaacademic.academicproject.membros.api.MembroRequestDTO;
import com.ligaacademic.academicproject.membros.api.MembroResponseDTO;
import com.ligaacademic.academicproject.membros.api.MembroUpdateRequestDTO;
import com.ligaacademic.academicproject.guildas.application.GuildaLookupService;
import com.ligaacademic.academicproject.guildas.domain.GuildasModel;
import com.ligaacademic.academicproject.membros.domain.Membro;
import com.ligaacademic.academicproject.shared.exceptions.ConflictException;
import com.ligaacademic.academicproject.membros.infra.MembroMapper;
import com.ligaacademic.academicproject.membros.infra.MembroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MembroService {


    private final MembroRepository membroRepository;
    private final GuildaLookupService guildaLookupService;
    private final MembroMapper membroMapper;

    public MembroService(MembroRepository membroRepository, GuildaLookupService guildaLookupService, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.guildaLookupService = guildaLookupService;
        this.membroMapper = membroMapper;
    }

    public MembroResponseDTO registrarMembro(MembroRequestDTO dto) {

        if(membroRepository.existsByMatricula(dto.matricula())){
            throw new ConflictException("Membro com a matricula registrada já existe");
        }

        Membro entidade = membroMapper.paraEntidade(dto);
        Membro salvo = membroRepository.save(entidade);
        return membroMapper.paraResponseDTO(salvo);

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

    public MembroResponseDTO atualizarMembro(String matricula, MembroUpdateRequestDTO dto) {

        Membro membroExistente = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Erro ao atualizar: matrícula " + matricula + " não encontrada"
                ));

        membroExistente.setNome(dto.nome());
        membroExistente.setEmail(dto.email());

        Membro salvo = membroRepository.save(membroExistente);
        return membroMapper.paraResponseDTO(salvo);

    }

    public MembroResponseDTO buscarMembro(String matricula){

       Membro entidade = membroRepository.findByMatriculaComTudo(matricula)
               .orElseThrow(() -> new EntityNotFoundException(
                       "Erro ao buscar: matrícula " + matricula + " não encontrada"
               ));

       return membroMapper.paraResponseDTO(entidade);
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> listarTodos(Pageable pageable){
        return membroRepository.findAll(pageable)
                .map(membroMapper::paraResponseDTO);
    }

    public void vincularMembroGuilda(String matricula, Long id) {
        Membro membro = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Membro com matrícula " + matricula + " não encontrado"
                ));
        GuildasModel guildas = guildaLookupService.buscarPorId(id);

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



