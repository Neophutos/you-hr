package com.youhr.application.data.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;

/**
 * @desc Das abstrakte Objekt AbstractID stellt primär die ID's für alle child-Objekte zur Verfügung.
 *
 * @attributes id
 *
 * @category Objekt
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-07-29
 */
@MappedSuperclass
public abstract class AbstractID {

    @Id
    @GeneratedValue
    @Type(type = "long")
    private Long id;

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractID)) {
            return false;
        }
        AbstractID other = (AbstractID) obj;

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }
}
