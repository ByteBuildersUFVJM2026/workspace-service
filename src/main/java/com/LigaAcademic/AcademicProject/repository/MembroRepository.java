package com.LigaAcademic.AcademicProject.repository;

import com.LigaAcademic.AcademicProject.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro,Long> {

    Optional <Membro> findByMatricula(String matricula);

    @Query("SELECT m FROM Membro m LEFT JOIN FETCH m.guildasModel ")
    List<Membro> buscarTodosComGuildas();

    @Query("SELECT m FROM Membro m LEFT JOIN FETCH m.guildasModel WHERE m.matricula = :matricula")
    Optional<Membro> findByMatriculaComGuildas(String matricula);

}





