package com.ligaacademic.academicproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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

    @Column(name = "nome_guilda")
    private String nomeGuilda;

    @Column(name = "tutor_guilda")
    private String tutorGuilda;

    @Column(name = "quantidade_pessoas")
    private int quantidadePessoas;

    @ManyToMany(mappedBy = "guildasModel")
    private List<Membro> membro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeGuilda() {
        return nomeGuilda;
    }

    public void setNomeGuilda(String nomeGuilda) {
        this.nomeGuilda = nomeGuilda;
    }

    public String getTutorGuilda() {
        return tutorGuilda;
    }

    public void setTutorGuilda(String tutorGuilda) {
        this.tutorGuilda = tutorGuilda;
    }

    public List<Membro> getMembro() {
        return membro;
    }

    public void setMembro(List<Membro> membro) {
        this.membro = membro;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    @Override
    public String toString() {
        return "GuildasModel{" +
                "id=" + id +
                ", nomeGuilda='" + nomeGuilda + '\'' +
                ", tutorGuilda='" + tutorGuilda + '\'' +
                ", quantidadePessoas=" + quantidadePessoas +
                '}';
    }
}
