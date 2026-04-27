package com.LigaAcademic.AcademicProject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "auditoria_logs")
@Entity(name = "AuditoriaLog")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String usuarioEmail;
    private String acao;
    private String nomeMetodo;
    private LocalDateTime dataHora;
}
