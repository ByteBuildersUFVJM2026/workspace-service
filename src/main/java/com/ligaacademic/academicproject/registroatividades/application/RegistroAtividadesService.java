package com.ligaacademic.academicproject.registroatividades.application;

import com.ligaacademic.academicproject.registroatividades.api.RegistroAtividadesRequestDTO;
import com.ligaacademic.academicproject.registroatividades.api.RegistroAtividadesResponseDTO;
import com.ligaacademic.academicproject.membros.application.MembroLookupService;
import com.ligaacademic.academicproject.registroatividades.infra.RegistroAtividadesMapper;
import com.ligaacademic.academicproject.membros.domain.Membro;
import com.ligaacademic.academicproject.registroatividades.domain.RegistroAtividades;
import com.ligaacademic.academicproject.registroatividades.infra.RegistroAtividadesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroAtividadesService {

    private final RegistroAtividadesRepository registroAtividadesRepository;
    private final MembroLookupService membroLookupService;
    private final RegistroAtividadesMapper registroAtividadesMapper;

    public RegistroAtividadesService(RegistroAtividadesRepository registroAtividadesRepository,
                                     MembroLookupService membroLookupService,
                                     RegistroAtividadesMapper registroAtividadesMapper) {
        this.registroAtividadesRepository = registroAtividadesRepository;
        this.membroLookupService = membroLookupService;
        this.registroAtividadesMapper = registroAtividadesMapper;
    }

    @Transactional
    public RegistroAtividadesResponseDTO registrarHoras(RegistroAtividadesRequestDTO dto) {
        List<Membro> membros = membroLookupService.buscarMembrosDiretoriaComLock(dto.matriculas());

        RegistroAtividades entidade = registroAtividadesMapper.horasParaEntidade(dto);
        membros.forEach(membro -> membro.setTotalHoras(membro.getTotalHoras().add(entidade.getHoras())));
        entidade.setParticipantes(membros);

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

        List<Membro> participantes = membroLookupService.buscarMembrosComLock(registro.getParticipantes().stream()
                .map(Membro::getMatricula)
                .toList());

        participantes.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras().subtract(registro.getHoras())));

        registroAtividadesRepository.delete(registro);
    }

    @Transactional
    public RegistroAtividadesResponseDTO atualizarRegistroAtividades(Long id, RegistroAtividadesRequestDTO dto) {
        RegistroAtividades registro = registroAtividadesRepository.findByIdComParticipantes(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro com id " + id + " nao encontrado"));

        List<Membro> participantesAtuais = membroLookupService.buscarMembrosComLock(registro.getParticipantes().stream()
                .map(Membro::getMatricula)
                .toList());

        List<Membro> novosParticipantes = membroLookupService.buscarMembrosDiretoriaComLock(dto.matriculas());

        participantesAtuais.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras().subtract(registro.getHoras())));

        novosParticipantes.forEach(membro ->
                membro.setTotalHoras(membro.getTotalHoras().add(dto.horas())));

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
