package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Mitarbeiter extends AbstractEntity {

    @NotEmpty
    private String vorname = "";

    @NotEmpty
    private String nachname = "";

    private String geburtsdatum;

    @Email
    private String email = "";

    private String telefonnr;

    private String position;

    private String abteilung;

    private String adresse;

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() { return nachname; }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getGeburtsdatum(){return geburtsdatum;}

    public void setGeburtsdatum(String geburtsdatum) { this.geburtsdatum = geburtsdatum;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public void setTelefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAbteilung() {
        return abteilung;
    }

    public void setAbteilung(String abteilung) {
        this.abteilung = abteilung;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}
