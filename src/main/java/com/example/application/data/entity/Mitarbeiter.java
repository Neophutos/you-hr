package com.example.application.data.entity;

import com.example.application.data.Role;
import com.example.application.data.generator.UserGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Mitarbeiter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    private String vorname = "";
    @NotEmpty
    private String nachname = "";
    private LocalDate geburtsdatum; //darf nicht @NotEmpty gesetzt werden
    @Email
    private String email = "";
    private String telefonnr = "";
    private String position = "";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "abteilung_id")
    private Abteilung abteilung;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id", referencedColumnName = "id")
    private Adresse adresse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rechteverwaltung_id", referencedColumnName = "id")
    private Rechteverwaltung rechteverwaltung;

    public Mitarbeiter() {
        this.rechteverwaltung = new Rechteverwaltung();
        this.user = new User();
    }

    public void generateUser() {
        if (vorname == null || nachname == null) {
            System.out.println("vorname und nachname müssen gesetzt werden, " +
                    "bevor ein neuer User Account generiert werden kann");
        } else {
            this.user.setName(String.format("%s %s", vorname, nachname));
            this.user.setUsername(String.format("%s.%s", vorname, nachname));
            this.user.setHashedPassword(UserGenerator.generateHashedPassword());
            this.user.setRoles(Set.of(Role.USER));
        }
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

    public LocalDate getGeburtsdatum(){return geburtsdatum;}

    public void setGeburtsdatum(LocalDate geburtsdatum) { this.geburtsdatum = geburtsdatum;}

    public void setRechteverwaltung(Rechteverwaltung rechteverwaltung) {
        this.rechteverwaltung = rechteverwaltung;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Abteilung getAbteilung() {
        return abteilung;
    }

    public void setAbteilung(Abteilung abteilung) {
        this.abteilung = abteilung;
    }

    public Team getTeam() {return team;}

    public void setTeam(Team team) {this.team = team;}

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public Rechteverwaltung getRechteverwaltung() {
        return rechteverwaltung;
    }

    @Override
    public String toString() {
        return "Mitarbeiter{" +
                "id=" + id +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", geburtsdatum=" + geburtsdatum +
                ", email='" + email + '\'' +
                ", telefonnr='" + telefonnr + '\'' +
                ", position='" + position + '\'' +
                ", abteilung='" + abteilung + '\'' +
                ", adresse=" + adresse +
                ", user=" + user +
                ", rechteverwaltung=" + rechteverwaltung +
                '}';
    }
}
