package com.LigaAcademic.AcademicProject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "guildas")
@AllArgsConstructor
@NoArgsConstructor
public class GuildasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nome_guilda;

    private String tutor_guilda;

    private int quantidade_pessoas;

    @ManyToMany(mappedBy = "guildasModel")
    private List<Membro> membro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome_guilda() {
        return nome_guilda;
    }

    public void setNome_guilda(String nome_guilda) {
        this.nome_guilda = nome_guilda;
    }

    public String getTutor_guilda() {
        return tutor_guilda;
    }

    public void setTutor_guilda(String tutor_guilda) {
        this.tutor_guilda = tutor_guilda;
    }

    public int getQuantidade_pessoas() {
        return quantidade_pessoas;
    }

    public void setQuantidade_pessoas(int quantidade_pessoas) {
        this.quantidade_pessoas = quantidade_pessoas;
    }

    @Override
    public String toString() {
        return "GuildasModel{" +
                "id=" + id +
                ", nome_guilda='" + nome_guilda + '\'' +
                ", tutor_guilda='" + tutor_guilda + '\'' +
                ", quantidade_pessoas=" + quantidade_pessoas +
                '}';
    }
}
