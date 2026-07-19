package com.ligaacademic.academicproject.registroatividades.infra;

import com.ligaacademic.academicproject.registroatividades.domain.RegistroAtividades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroAtividadesRepository extends JpaRepository<RegistroAtividades, Long> {

    @Query("SELECT DISTINCT r FROM RegistroAtividades r LEFT JOIN FETCH r.participantes")
    List<RegistroAtividades> findAllComParticipantes();

    @Query("SELECT DISTINCT r FROM RegistroAtividades r LEFT JOIN FETCH r.participantes p WHERE p.matricula = :matricula")
    List<RegistroAtividades> findByParticipanteMatricula(@Param("matricula") String matricula);

    @Query("SELECT r FROM RegistroAtividades r LEFT JOIN FETCH r.participantes WHERE r.id = :id")
    Optional<RegistroAtividades> findByIdComParticipantes(@Param("id") Long id);

}

