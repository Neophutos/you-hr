package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Rechteverwaltung extends AbstractEntity {

    private String vorname;
    private String nachname;
    private String mitarbeiterid;
    private boolean lesen;
    private boolean erstellen;
    private boolean bearbeiten;
    private boolean loeschen;
    private boolean admin;

    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public String getMitarbeiterid() {
        return mitarbeiterid;
    }
    public void setMitarbeiterid(String mitarbeiterid) {
        this.mitarbeiterid = mitarbeiterid;
    }
    public boolean isLesen() {
        return lesen;
    }
    public void setLesen(boolean lesen) {
        this.lesen = lesen;
    }
    public boolean isErstellen() {
        return erstellen;
    }
    public void setErstellen(boolean erstellen) {
        this.erstellen = erstellen;
    }
    public boolean isBearbeiten() {
        return bearbeiten;
    }
    public void setBearbeiten(boolean bearbeiten) {
        this.bearbeiten = bearbeiten;
    }
    public boolean isLoeschen() {
        return loeschen;
    }
    public void setLoeschen(boolean loeschen) {
        this.loeschen = loeschen;
    }
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
