package com.LigaAcademic.AcademicProject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
@Table(name = "contabilhoras")
@NoArgsConstructor
@AllArgsConstructor
public class ContabilHoras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float horas;

    @Column(name = "tipoatividade")
    private String tipoAtividade;

    @Column(name = "setoratividade")
    private String setorAtividade;

    @Column(name ="descatividade")
    private String descAtividade;


    @Column(name = "dataatividade")
    private LocalDate dataAtividade;

    @Column(name = "participantes")
    private String participantes;

    @Column(name = "matricula")
    private String matricula;

    public float getHoras() {
        return horas;
    }

    public void setHoras(float horas) {
        this.horas = horas;
    }

    public String getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(String tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public String getDescAtividade() {
        return descAtividade;
    }

    public void setDescAtividade(String descAtividade) {
        this.descAtividade = descAtividade;
    }

    public String getSetorAtividade() {
        return setorAtividade;
    }

    public void setSetorAtividade(String setorAtividade) {
        this.setorAtividade = setorAtividade;
    }

    public LocalDate getDataAtividade() {
        return dataAtividade;
    }

    public void setDataAtividade(LocalDate dataAtividade) {
        this.dataAtividade = dataAtividade;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "ContabilHoras{" +
                "id=" + id +
                ", horas=" + horas +
                ", tipoAtividade='" + tipoAtividade + '\'' +
                ", setorAtividade='" + setorAtividade + '\'' +
                ", descAtividade='" + descAtividade + '\'' +
                ", dataAtividade=" + dataAtividade +
                ", participantes='" + participantes + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
