package com.ligaacademic.academicproject.membros.application;

import com.ligaacademic.academicproject.membros.domain.Membro;
import com.ligaacademic.academicproject.membros.infra.MembroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MembroLookupService {

    private final MembroRepository membroRepository;

    public MembroLookupService(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    public List<Membro> buscarMembrosComLock(List<String> matriculas) {
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
