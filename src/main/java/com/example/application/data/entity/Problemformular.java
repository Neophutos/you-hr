package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Problemformular extends AbstractEntity {

    @NotEmpty
    private String datum;
    @NotEmpty
    private String problemart;
    @NotEmpty
    private String beschreibung;

    public String getdatum() {
        return datum;
    }
    public void setDatum(String datum) {
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

}
