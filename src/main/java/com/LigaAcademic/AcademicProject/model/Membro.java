package com.LigaAcademic.AcademicProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membros")
@NoArgsConstructor
@AllArgsConstructor
public class Membro {

    private String nome;

    private String cargo;

    @Column(unique = true,length = 11)
    private String matricula;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @ManyToMany
    @JoinTable(name = "relacao_guildas",
    joinColumns = @JoinColumn(name = "membro_id"),
    inverseJoinColumns = @JoinColumn(name = "guilda_model_id")
    )
    private List<GuildasModel> guildasModel = new ArrayList<>();




    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GuildasModel> getGuildasModel() {
        return guildasModel;
    }

    public void setGuildasModel(List<GuildasModel> guildasModel) {
        this.guildasModel = guildasModel;
    }

    @Override
    public String toString() {
        return "Membro{" +
                "nome='" + nome + '\'' +
                ", cargo='" + cargo + '\'' +
                ", matricula='" + matricula + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", guildasModel=" + guildasModel +
                '}';
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }



}
