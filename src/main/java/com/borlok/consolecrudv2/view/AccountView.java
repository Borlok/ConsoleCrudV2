package com.borlok.consolecrudv2.view;

import com.borlok.consolecrudv2.controller.AccountController;
import com.borlok.consolecrudv2.controller.CustomerController;
import com.borlok.consolecrudv2.controller.SpecialtyController;
import com.borlok.consolecrudv2.model.Account;
import com.borlok.consolecrudv2.model.AccountStatus;
import com.borlok.consolecrudv2.model.Specialty;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AccountView {
    private final CustomerController customerController;
    private final SpecialtyController specialityController;
    private final AccountController accountController;
    private Scanner sc;

    public AccountView() {
        customerController = new CustomerController();
        specialityController = new SpecialtyController();
        accountController = new AccountController();
    }


    public void main() {
        try {
            sc = new Scanner(System.in);
            int choice;
            System.out.println("\n--Аккаунт--\n" +
                    "Выберите действие:\n" +
                    "1: Добавить аккаунт\n" +
                    "2: Посмотреть аккаунты\n" +
                    "3: Редактировать аккаунт\n" +
                    "4: Удалить аккаунт\n" +
                    "5: Назад");
            choice = sc.nextInt();
            while (true)
                switch (choice) {
                    case 1 -> create();
                    case 2 -> get();
                    case 3 -> update();
                    case 4 -> delete();
                    case 5 -> back();
                    default -> {
                        System.out.print("\nТакого действия нет");
                        main();
                    }
                }
        } catch (Exception e) {
            System.out.println("Введены неверные символы");
            main();
        } finally {
            sc.close();
        }
    }

    private void create() {
        customerController.create(addSpecialties(), addAccount());
        main();
    }

    private Set<Specialty> addSpecialties() {
        Set<Specialty> specialties = new HashSet<>();
        System.out.println("Добавьте специальности\n" +
                "для выхода нажмите '0'");
        getSpecialtyList();
        long order = sc.nextLong();
        while (order != 0) {
            specialties.add(specialityController.getById(order));
            System.out.println("добавьте еще или нажмите '0' для продолжения");
            order = sc.nextLong();
        }
        return specialties;
    }

    private void getSpecialtyList() {
        specialityController.read().forEach(System.out::println);
    }

    private Account addAccount() {
        System.out.println("Введите имя аккаунта:");
        return accountController.create(sc.next());
    }

    private void get() {
        getAll();
        System.out.println("Введите любой символ для продолжения...");
        sc.next();
        main();
    }

    private void getAll() {
        for (Account x : accountController.read())
            System.out.println("id: " + x.getId() +
                    " | Имя: " + x.getName() +
                    " | Статус: " + x.getAccountStatus());
    }

    private void update() {
        System.out.println("--Выберите аккаунт для замены--");
        getAll();
        long choice = sc.nextLong();
        accountController.update(updateAccountById(choice),choice);
        main();
    }

    private Account updateAccountById(Long id) {
        System.out.println("Введите новое имя аккаунта: ");
        Account account = new Account(sc.next());
        System.out.println("Измените статус аккаунта: ");
        for (int i = 0; i < 2; i++) {
            System.out.println((i + 1) + ": " +AccountStatus.values()[i]);
        }
        switch (sc.nextInt()) {
            case 1 -> account.setAccountStatus(AccountStatus.values()[0]);
            case 2 -> account.setAccountStatus(AccountStatus.values()[1]);
            default -> System.out.println("Такого варианта нет");
        }
        return account;
    }

    private void delete() {
        System.out.println("Выберите аккаунт для удаления:");
        getAll();
        long order = sc.nextLong();
        accountController.delete(order);
        customerController.delete(order);
        main();
    }

    private void back() {
        new View().main();
    }
}
