package com.youhr.application.data.entity;

import com.youhr.application.data.generator.DataGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @desc Das Objekt Antrag stellt einen vollständigen Antrag/Problem im System dar, den ein Nutzer erstellt (antragstellername)
 *
 * @attributes datum, antragstellername, antragsart, beschreibung
 *
 * @mappedattributes id
 *
 * @category Objekt
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
public class Antrag extends AbstractID {

    private LocalDate datum;

    private String antragstellername = "";

    @NotEmpty
    private String antragsart = "";

    @NotEmpty
    private String beschreibung = "";


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @NotNull
    private Status status;

    public Antrag() {

    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getAntragsart() {
        return antragsart;
    }

    public void setAntragsart(String antragsart) {
        this.antragsart = antragsart;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getAntragstellername() {
        return antragstellername;
    }

    public void setAntragstellername(String antragstellername) {
        this.antragstellername = antragstellername;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

}
