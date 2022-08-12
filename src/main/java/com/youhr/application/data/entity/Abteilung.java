package com.youhr.application.data.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc Das Objekt Abteilung stellt die Aufteilung des Unternehmens in deren Bereiche dar.
 *
 * @attributes bezeichnung
 *
 * @mappedattributes id, mitarbeiter
 *
 * @category Objekt
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
public class Abteilung extends AbstractID {

    @NotBlank
    private String bezeichnung;

    @OneToMany(mappedBy = "abteilung")
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
