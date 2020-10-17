package com.borlok.consolecrudv2.model;

import java.util.Set;

public class Customer {
    private int id;
    private Account account;
    private Set<Specialty> specialties;

    public Customer(Set<Specialty> specialties, Account account) {
        this.account = account;
        this.specialties = specialties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        this.specialties = specialties;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
