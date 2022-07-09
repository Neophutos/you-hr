package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @desc Das Objekt Rechteverwaltung realisiert die Interaktionsberechtigungen im System.
 *
 * @attributes lesen, erstellen, bearbeiten, loeschen, admin
 *
 * @mappedattributes mitarbeiter
 *
 * @category Objekt
 * @version 1.0
 * @since 2022-07-06
 */
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
    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    @Override
    public String toString() {
        return "Rechteverwaltung{" +
                ", lesen=" + lesen +
                ", erstellen=" + erstellen +
                ", bearbeiten=" + bearbeiten +
                ", loeschen=" + loeschen +
                ", admin=" + admin +
                '}';
    }
}
