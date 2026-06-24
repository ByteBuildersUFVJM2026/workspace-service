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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<Membro> membros = buscarMembrosComLock(dto.matriculas());

        RegistroAtividades entidade = registroAtividadesMapper.horasParaEntidade(dto);
        membros.forEach(membro -> membro.setTotalHoras(membro.getTotalHoras().add(entidade.getHoras())));
        entidade.setParticipantes(membros);

        membroRepository.saveAll(membros);
        RegistroAtividades salvo = registroAtividadesRepository.save(entidade);
        RegistroAtividades salvoComParticipantes = registroAtividadesRepository.findByIdComParticipantes(salvo.getId()).orElseThrow();
        return registroAtividadesMapper.horasParaResponseDTO(salvoComParticipantes);
    }

    @Transactional
    public List<RegistroAtividadesResponseDTO> listarAtividadesParticipante(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("Matricula invalida");
        }

        List<RegistroAtividades> listaDeAtividades = registroAtividadesRepository.findByParticipanteMatricula(matricula);

        if (listaDeAtividades.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma atividade encontrada para a matricula " + matricula);
        }

        return listaDeAtividades.stream()
                .map(registroAtividadesMapper::horasParaResponseDTO)
                .toList();
    }

    @Transactional
    public Page<RegistroAtividadesResponseDTO> listarTodos(Pageable pageable) {
        return registroAtividadesRepository.findAll(pageable)
                .map(registroAtividadesMapper::horasParaResponseDTO);
    }

    @Transactional
    public void apagarRegistro(Long idParaRemover) {
        RegistroAtividades registro = registroAtividadesRepository.findByIdComParticipantes(idParaRemover)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + idParaRemover + " nao encontrado"));

        List<Membro> participantes = buscarMembrosComLock(registro.getParticipantes().stream()
                .map(Membro::getMatricula)
                .toList());

        participantes.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras().subtract(registro.getHoras())));

        membroRepository.saveAll(participantes);
        registroAtividadesRepository.delete(registro);
    }

    @Transactional
    public RegistroAtividadesResponseDTO atualizarRegistroAtividades(Long id, RegistroAtividadesRequestDTO dto) {
        RegistroAtividades registro = registroAtividadesRepository.findByIdComParticipantes(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + id + " nao encontrado"));

        List<Membro> participantesAtuais = buscarMembrosComLock(registro.getParticipantes().stream()
                .map(Membro::getMatricula)
                .toList());

        participantesAtuais.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras().subtract(registro.getHoras())));
        membroRepository.saveAll(participantesAtuais);

        List<Membro> novosParticipantes = buscarMembrosComLock(dto.matriculas());

        novosParticipantes.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras().add(dto.horas())));
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

    private List<Membro> buscarMembrosComLock(List<String> matriculas) {
        if (matriculas == null || matriculas.isEmpty()) {
            return List.of();
        }

        List<String> matriculasUnicas = new LinkedHashSet<>(matriculas).stream().toList();
        List<Membro> membros = membroRepository.findAllByMatriculaInForUpdate(matriculasUnicas);

        if (membros.size() != matriculasUnicas.size()) {
            var encontrados = membros.stream()
                    .map(Membro::getMatricula)
                    .collect(Collectors.toSet());

            String matriculaNaoEncontrada = matriculasUnicas.stream()
                    .filter(matricula -> !encontrados.contains(matricula))
                    .findFirst()
                    .orElse("desconhecida");

            throw new EntityNotFoundException("Membro com matricula " + matriculaNaoEncontrada + " nao encontrado");
        }

        var membrosPorMatricula = membros.stream()
                .collect(Collectors.toMap(Membro::getMatricula, Function.identity()));

        return matriculasUnicas.stream()
                .map(membrosPorMatricula::get)
                .toList();
    }
}
