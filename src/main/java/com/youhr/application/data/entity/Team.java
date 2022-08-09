package com.youhr.application.data.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc Das Objekt Team stellt die Aufteilung des Unternehmens in deren Projekte bzw. Arbeitsgruppen dar. Teams können aus Mitarbeiern aus versch. Bereichen sein, dies ist aber nicht zwingend erforderlich.
 *
 * @attributes bezeichnung
 *
 * @mappedattributes id, mitarbeiter
 *
 * @category Objekt
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
public class Team extends AbstractEntity{

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
