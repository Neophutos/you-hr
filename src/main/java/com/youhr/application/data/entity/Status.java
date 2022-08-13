package com.youhr.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Status extends AbstractID {

    private String name;

    public Status() { }

    public Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
