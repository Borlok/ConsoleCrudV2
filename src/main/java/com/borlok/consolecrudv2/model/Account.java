package com.borlok.consolecrudv2.model;

public class Account {
    private int id;
    private String name;
    private AccountStatus accountStatus;

    public Account(String name) {
        this.name = name;
        id = 0;
        accountStatus = AccountStatus.ACTIVE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
