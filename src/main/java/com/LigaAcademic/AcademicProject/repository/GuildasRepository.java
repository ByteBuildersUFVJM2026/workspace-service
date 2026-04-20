package com.LigaAcademic.AcademicProject.repository;

import com.LigaAcademic.AcademicProject.model.GuildasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuildasRepository extends JpaRepository<GuildasModel, Long> {

    @Query("SELECT DISTINCT g FROM GuildasModel g LEFT JOIN FETCH g.membro WHERE g.id = :id")
    Optional<GuildasModel> findByIdComMembros(@Param("id") Long id);

    @Query("SELECT DISTINCT g FROM GuildasModel g LEFT JOIN FETCH g.membro")
    List<GuildasModel> findAllComMembros();
}
