package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Rechteverwaltung extends AbstractEntity {

    @OneToOne(mappedBy = "rechteverwaltung")
    private Mitarbeiter mitarbeiter;

    private boolean lesen;
    private boolean erstellen;
    private boolean bearbeiten;
    private boolean loeschen;
    private boolean admin;



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
