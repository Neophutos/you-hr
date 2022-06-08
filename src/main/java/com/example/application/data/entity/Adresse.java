package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private int plz;

    private String strassenname;

    private int hausnummer;

    private String stadt;

    private String bundesland;

    @OneToOne(mappedBy = "adresse")
    private Mitarbeiter mitarbeiter;

    public Adresse(){

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
