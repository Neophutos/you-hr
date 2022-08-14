package com.youhr.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc Das Objekt Status stellt den derzeitigen Bearbeitungsstand eines Antrags dar.
 *
 * @attributes name
 *
 * @mappedattributes id, antraege
 *
 * @category Objekt
 * @author Ben Köppe, Tim Freund, Riccardo Prochnow
 * @version 1.0
 * @since 2022-08-10
 */
@Entity
public class Status extends AbstractID {

    private String name;

    @OneToMany(mappedBy = "status")
    private final List<Antrag> antraege = new LinkedList<>();

    public Status() {}

    public Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
