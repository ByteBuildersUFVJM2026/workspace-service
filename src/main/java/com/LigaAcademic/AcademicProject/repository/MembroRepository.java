package com.LigaAcademic.AcademicProject.repository;

import com.LigaAcademic.AcademicProject.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro,Long> {

    Optional <Membro> findByMatricula(String matricula);

}





