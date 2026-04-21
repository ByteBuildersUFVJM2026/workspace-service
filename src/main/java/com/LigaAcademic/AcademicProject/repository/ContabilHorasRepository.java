package com.LigaAcademic.AcademicProject.repository;

import com.LigaAcademic.AcademicProject.model.ContabilHoras;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContabilHorasRepository extends JpaRepository<ContabilHoras,Long> {

    List<ContabilHoras> findByMatricula(String matricula);

}
