package com.youhr.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @desc Das Objekt Adresse stellt eine vollständige Anschrift (in Deutschland) dar.
 *
 * @attributes plz, strassenname, stadt, bundesland
 *
 * @mappedattributes id, mitarbeiter
 *
 * @category Objekt
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
public class Adresse extends AbstractID {

    @NotNull
    private int plz = 0;

    @NotEmpty
    private String strassenname = "";

    @NotNull
    private int hausnummer;

    @NotEmpty
    private String stadt = "";

    @NotEmpty
    private String bundesland;

    @OneToOne(mappedBy = "adresse")
    private Mitarbeiter mitarbeiter;

    public Adresse() {

    }

    public Adresse(int plz, String strassenname, int hausnummer, String stadt, String bundesland) {
        this.plz = plz;
        this.strassenname = strassenname;
        this.hausnummer = hausnummer;
        this.stadt = stadt;
        this.bundesland = bundesland;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getStrassenname() {
        return strassenname;
    }

    public void setStrassenname(String strassenname) {
        this.strassenname = strassenname;
    }

    public int getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(int hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public String getBundesland() {
        return bundesland;
    }

    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    @Override
    public String toString() {
        return strassenname + " " + hausnummer + ", " + plz + " " + stadt + ", " + bundesland;
    }
}
