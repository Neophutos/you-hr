package com.example.application.data.entity;

import com.example.application.data.generator.UserGenerator;
import com.vaadin.flow.component.notification.Notification;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private String abteilung = "";

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
    }

    public void generateUser() {
        if (vorname == null || nachname == null) {
            System.out.println("vorname und nachname müssen gesetzt werden, " +
                    "bevor ein neuer User Account generiert werden kann");
        }
        this.user = new User();
        this.user = UserGenerator.generate(vorname, nachname, this.user);
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

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public User getUser() {
        return user;
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
                ", geburtsdatum='" + geburtsdatum + '\'' +
                ", email='" + email + '\'' +
                ", telefonnr='" + telefonnr + '\'' +
                ", position='" + position + '\'' +
                ", abteilung='" + abteilung + '\'' +
                ", adresse=" + adresse +
                '}';
    }



}
