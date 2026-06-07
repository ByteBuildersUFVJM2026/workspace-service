package com.ligaacademic.academicproject.service;

import com.ligaacademic.academicproject.dto.RegistroAtividadesRequestDTO;
import com.ligaacademic.academicproject.dto.RegistroAtividadesResponseDTO;
import com.ligaacademic.academicproject.mapper.RegistroAtividadesMapper;
import com.ligaacademic.academicproject.model.Membro;
import com.ligaacademic.academicproject.model.RegistroAtividades;
import com.ligaacademic.academicproject.repository.MembroRepository;
import com.ligaacademic.academicproject.repository.RegistroAtividadesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroAtividadesService {

    private final RegistroAtividadesRepository registroAtividadesRepository;
    private final MembroRepository membroRepository;
    private final RegistroAtividadesMapper registroAtividadesMapper;

    public RegistroAtividadesService(RegistroAtividadesRepository registroAtividadesRepository,
                                     MembroRepository membroRepository,
                                     RegistroAtividadesMapper registroAtividadesMapper) {
        this.registroAtividadesRepository = registroAtividadesRepository;
        this.membroRepository = membroRepository;
        this.registroAtividadesMapper = registroAtividadesMapper;
    }

    @Transactional
    public RegistroAtividadesResponseDTO registrarHoras(RegistroAtividadesRequestDTO dto) {
        List<Membro> membros = dto.matriculas().stream()
                .map(matricula -> membroRepository.findByMatricula(matricula)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Membro com matrícula " + matricula + " não encontrado")))
                .toList();

        RegistroAtividades entidade = registroAtividadesMapper.horasParaEntidade(dto);
        membros.forEach(membro -> membro.setTotalHoras(membro.getTotalHoras() + entidade.getHoras()));
        entidade.setParticipantes(membros);

        membroRepository.saveAll(membros);
        RegistroAtividades salvo = registroAtividadesRepository.save(entidade);
        RegistroAtividades salvoComParticipantes = registroAtividadesRepository.findByIdComParticipantes(salvo.getId()).orElseThrow();
        return registroAtividadesMapper.horasParaResponseDTO(salvoComParticipantes);
    }

    @Transactional
    public List<RegistroAtividadesResponseDTO> listarAtividadesParticipante(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula inválida");
        }

        List<RegistroAtividades> listaDeAtividades = registroAtividadesRepository.findByParticipanteMatricula(matricula);

        if (listaDeAtividades.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma atividade encontrada para a matrícula " + matricula);
        }

        return listaDeAtividades.stream()
                .map(registroAtividadesMapper::horasParaResponseDTO)
                .toList();
    }

    @Transactional
    public List<RegistroAtividadesResponseDTO> listarTodos() {
        return registroAtividadesRepository.findAllComParticipantes()
                .stream()
                .map(registroAtividadesMapper::horasParaResponseDTO)
                .toList();
    }

    @Transactional
    public void apagarRegistro(Long idParaRemover) {
        RegistroAtividades registro = registroAtividadesRepository.findByIdComParticipantes(idParaRemover)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + idParaRemover + " não encontrado"));

        registro.getParticipantes().forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras() - registro.getHoras()));

        membroRepository.saveAll(registro.getParticipantes());
        registroAtividadesRepository.delete(registro);
    }

    @Transactional
    public RegistroAtividadesResponseDTO atualizarRegistroAtividades(Long id, RegistroAtividadesRequestDTO dto) {
        RegistroAtividades registro = registroAtividadesRepository.findByIdComParticipantes(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + id + " não encontrado"));

        registro.getParticipantes().forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras() - registro.getHoras()));
        membroRepository.saveAll(registro.getParticipantes());

        List<Membro> novosParticipantes = dto.matriculas().stream()
                .map(matricula -> membroRepository.findByMatricula(matricula)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Membro com matrícula " + matricula + " não encontrado")))
                .toList();

        novosParticipantes.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras() + dto.horas()));
        membroRepository.saveAll(novosParticipantes);

        registro.setHoras(dto.horas());
        registro.setTipoAtividade(dto.tipoAtividade());
        registro.setSetorAtividade(dto.setorAtividade());
        registro.setDescAtividade(dto.descAtividade());
        registro.setDataAtividade(dto.dataAtividade());
        registro.setParticipantes(novosParticipantes);

        registroAtividadesRepository.save(registro);
        return registroAtividadesMapper.horasParaResponseDTO(
                registroAtividadesRepository.findByIdComParticipantes(id).orElseThrow());
    }
}
