package com.example.application.data.entity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Mitarbeiter extends AbstractEntity {

    private String mitarbeiterid;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String email;
    private String telefonnr;
    private String position;
    private String abteilung;
    private String adresse;

    public String getMitarbeiterid() {
        return mitarbeiterid;
    }
    public void setMitarbeiterid(String mitarbeiterid) {
        this.mitarbeiterid = mitarbeiterid;
    }
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
