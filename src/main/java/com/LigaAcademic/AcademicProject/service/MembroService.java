package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.DTO.MembroRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.MembroResponseDTO;
import com.LigaAcademic.AcademicProject.DTO.MembroUpdateRequestDTO;
import com.LigaAcademic.AcademicProject.Infra.Exceptions.ConflictException;
import com.LigaAcademic.AcademicProject.Mapper.MembroMapper;
import com.LigaAcademic.AcademicProject.model.GuildasModel;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MembroService {


    private final MembroRepository membroRepository;
    private final GuildasRepository guildasRepository;
    private final MembroMapper membroMapper;

    public MembroService(MembroRepository membroRepository, GuildasRepository guildasRepository, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.guildasRepository = guildasRepository;
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

    public List<MembroResponseDTO> listarTodos(){
        return membroRepository.buscarTodosComGuildas()
                .stream()
                .map(membroMapper::paraResponseDTO)
                .toList();
    }

    public void vincularMembroGuilda(String matricula, Long id) {
        Membro membro = membroRepository.findByMatriculaComTudo(matricula)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Membro com matrícula " + matricula + " não encontrado"
                ));
        GuildasModel guildas = guildasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Guilda com id " + id + " não encontrada"
                ));

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


