package com.borlok.consolecrudv2.model;

public class Specialty {
    private int id;
    private String name;

    public Specialty(String name) {
        this.name = name;
        id = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id = " + id +
                ", name = " + name;
    }
}
