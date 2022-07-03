package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
public class Antrag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private LocalDate datum; //darf nicht @NotEmpty gesetzt werden

    private String antragstellername = "";
    @NotEmpty
    private String antragsart = "";
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

    public String getAntragsart() {
        return antragsart;
    }

    public void setAntragsart(String antragsart) {
        this.antragsart = antragsart;
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
                ", antragsart='" + antragsart + '\'' +
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
