package com.youhr.application.data.entity;

import com.youhr.application.security.Role;
import com.youhr.application.data.generator.UserGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * @desc Das Objekt Mitarbeiter stellt die vollständigen Daten des Mitarbeiters im Unternehmen dar.
 *
 * @attributes vorname, nachname, geburtsdatum, email, telefonnr, position
 *
 * @mappedattributes id, abteilung, team, adresse, user, rechteverwaltung
 *
 * @category Objekt
 * @author Ben Köppe, Tim Freund, Chris Zobel, Riccardo Prochnow
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
public class Mitarbeiter extends AbstractID {

    @NotEmpty
    private String vorname = "";

    @NotEmpty
    private String nachname = "";

    @NotNull
    private LocalDate geburtsdatum;

    @Email
    @NotNull
    private String email = "";

    @NotEmpty
    private String telefonnr = "";

    @NotEmpty
    private String position = "";

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "abteilung_id", referencedColumnName = "id")
    private Abteilung abteilung;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id", referencedColumnName = "id")
    private Adresse adresse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_user_id", referencedColumnName = "id")
    private User user;

    public Mitarbeiter() {
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
            this.user.setRoles(Set.of(Role.MITARBEITER));
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

}
