package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private int plz;
    @NotEmpty
    private String strassenname;
    @NotEmpty
    private int hausnummer;
    @NotEmpty
    private String stadt;
    @NotEmpty
    private String Bundesland;

    @OneToOne(fetch = FetchType.LAZY)
    private Mitarbeiter mitarbeiter;


    public Long getId() {
        return id;
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
        return Bundesland;
    }

    public void setBundesland(String bundesland) {
        Bundesland = bundesland;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }
}
