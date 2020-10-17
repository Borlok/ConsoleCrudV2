package com.borlok.consolecrudv2.repository.io;

import com.borlok.consolecrudv2.model.Account;
import com.borlok.consolecrudv2.model.AccountStatus;
import com.borlok.consolecrudv2.repository.AccountRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRepositoryImpl implements AccountRepository {
    private final String FILE_PATH = "./src/main/resources/files/accounts.txt";
    private final Path PATH = Paths.get(FILE_PATH);
    private static AccountRepository accountRepository;

    private AccountRepositoryImpl() {
    }

    public static AccountRepository getInstance() {
        if (accountRepository == null)
            accountRepository = new AccountRepositoryImpl();
        return accountRepository;
    }

    @Override
    public Account create(Account account) {
        List<Account> allAccountsList = getAll();
        allAccountsList.add(account);
        saveCollectionOfAccountToFile(allAccountsList);
        return account;
    }

    private void saveCollectionOfAccountToFile(List<Account> allAccountsList) {
        try (FileWriter writer = new FileWriter(PATH.toFile())) {
            for (Account account : allAccountsList)
                writer.write(account.getId() + "," + account.getName() + "," + account.getAccountStatus() + "\n");
        } catch (IOException e) {
            System.err.println("Проблема при записи аккаунта в файл: " + e);
        }
    }

    @Override
    public Account getById(Long id) {
        return getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(isNotInRepository());
    }

    public Account isNotInRepository () {
        Account isNotInRepository = new Account("Удален");
        isNotInRepository.setAccountStatus(AccountStatus.DELETED);
        return isNotInRepository;
    }

    @Override
    public List<Account> getAll() {
        List<Account> returnedList = new ArrayList<>();
        try {
            List<String> specialtiesList = Files.readAllLines(PATH);
            returnedList = specialtiesList.stream()
                    .filter(x -> !(x.equals("")))
                    .map(x -> x.split(","))
                    .map(x -> {Account accountToReturnedList = new Account(x[1]);
                        accountToReturnedList.setId(Integer.parseInt(x[0]));
                        accountToReturnedList.setAccountStatus(AccountStatus.valueOf(x[2]));
                        return accountToReturnedList;})
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ошибка при получении аккаунтов из файла: " + e);
        }
        return returnedList;
    }

    @Override
    public Account update(Account account, Long id) {
        List<Account> allAccountsList = getAll();
        allAccountsList.set(allAccountsList.indexOf(allAccountsList.stream()
                .filter(x -> x.getId() == id).findFirst().orElse(isNotInRepository())),account);
        saveCollectionOfAccountToFile(allAccountsList);
        return account;
    }

    @Override
    public void delete(Long id) {
        List<Account> allAccountsList = getAll();
        allAccountsList.remove(allAccountsList.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(isNotInRepository()));
        saveCollectionOfAccountToFile(allAccountsList);
    }
}
