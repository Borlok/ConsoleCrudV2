package com.borlok.consolecrudv2.controller;

import com.borlok.consolecrudv2.model.Account;
import com.borlok.consolecrudv2.repository.AccountRepository;
import com.borlok.consolecrudv2.repository.io.AccountRepositoryImpl;

import java.util.List;

public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController() {
        accountRepository = AccountRepositoryImpl.getInstance();
    }

    public Account create(String accountName) {
        Account account = new Account(accountName);

        List<Account> accountList = accountRepository.getAll();
        if (accountList.isEmpty())
            account.setId(1);
        else
            account.setId(accountList.get(accountList.size() - 1).getId() + 1);
        return accountRepository.create(account);
    }

    public List<Account> read() {
        return accountRepository.getAll();
    }

    public Account getById(Long id) {
        return accountRepository.getById(id);
    }

    public Account update (Account account, Long id) {
        account.setId(id.intValue());
        return accountRepository.update(account, id);
    }

    public void delete(Long id) {
        accountRepository.delete(id);
    }
}
