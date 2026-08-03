package com.ligaacademic.academicproject.registroatividades.domain;


import com.ligaacademic.academicproject.membros.domain.Membro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contabilhoras")
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAtividades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal horas;

    @Column(name = "tipoatividade")
    private String tipoAtividade;

    @Column(name = "setoratividade")
    private String setorAtividade;

    @Column(name ="descatividade")
    private String descAtividade;


    @Column(name = "dataatividade")
    private LocalDate dataAtividade;

    @ManyToMany
    @JoinTable(
            name = "membro_atividade",
            joinColumns = @JoinColumn(name = "atividade_id"),
            inverseJoinColumns = @JoinColumn(name = "membro_id")
    )
    private List<Membro> participantes = new ArrayList<>();

    public BigDecimal getHoras() {
        return horas;
    }

    public void setHoras(BigDecimal horas) {
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

    public List<Membro> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Membro> participantes) {
        this.participantes = participantes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RegistroAtividades{" +
                "id=" + id +
                ", horas=" + horas +
                ", tipoAtividade='" + tipoAtividade + '\'' +
                ", setorAtividade='" + setorAtividade + '\'' +
                ", descAtividade='" + descAtividade + '\'' +
                ", dataAtividade=" + dataAtividade +
                ", participantes=" + participantes +
                '}';
    }
}

