package com.example.application.data.entity;

import com.example.application.security.AuthenticatedUser;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private LocalDate datum; //darf nicht @NotEmpty gesetzt werden

    private String antragstellername = "";
    @NotEmpty
    private String problemart = "";
    @NotEmpty
    private String beschreibung = "";

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getProblemart() {
        return problemart;
    }

    public void setProblemart(String problemart) {
        this.problemart = problemart;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }


    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", datum=" + datum +
                ", problemart='" + problemart + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }

    public String getAntragstellername() {
        return antragstellername;
    }

    public void setAntragstellername(String antragstellername) {
        this.antragstellername = antragstellername;
    }
}
