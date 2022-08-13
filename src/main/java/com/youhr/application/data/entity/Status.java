package com.youhr.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Status extends AbstractID {

    private String name;

    @OneToMany(mappedBy = "status")
    private List<Antrag> antraege = new LinkedList<>();

    public Status() {
    }

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
