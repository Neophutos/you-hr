package com.example.application.data.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc Das Objekt Abteilung stellt die Aufteilung des Unternehmens in deren Bereiche dar. Teams können aus Mitarbeiern aus versch. Bereichen sein, dies ist aber nicht zwingend erforderlich.
 *
 * @attributes id, bezeichnung
 *
 * @mappedattributes mitarbeiter
 *
 * @category Objekt
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String bezeichnung;

    @OneToMany(mappedBy = "team")
    private List<Mitarbeiter> mitarbeiter = new LinkedList<>();

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public List<Mitarbeiter> getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(List<Mitarbeiter> mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }
}
