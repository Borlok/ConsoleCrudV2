package com.borlok.consolecrudv2.view;

import com.borlok.consolecrudv2.controller.AccountController;
import com.borlok.consolecrudv2.controller.CustomerController;
import com.borlok.consolecrudv2.controller.SpecialtyController;
import com.borlok.consolecrudv2.model.Account;
import com.borlok.consolecrudv2.model.Customer;
import com.borlok.consolecrudv2.model.Specialty;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CustomerView {
    private final CustomerController customerController;
    private final SpecialtyController specialityController;
    private final AccountController accountController;
    private Scanner sc;

    public CustomerView() {
        customerController = new CustomerController();
        specialityController = new SpecialtyController();
        accountController = new AccountController();
    }

    public void main() {
        sc = new Scanner(System.in);
        System.out.println("--Покупатели--\n" +
                "Выберите действие:\n" +
                "1: Создать покупателя\n" +
                "2: Посмотреть покупателей\n" +
                "3: Редактировать покупателя\n" +
                "4: Удалить покупателя\n" +
                "5: Назад");
        try {
            int choice = sc.nextInt();
            while (true) {
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
            }
        } catch (Exception e) {
            System.out.println("Введены неверные символы: " + e);
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
        for (Customer x : customerController.read())
            System.out.println("id: " + x.getId() +
                    " | Имя: " + x.getAccount().getName() +
                    " | Специальности: [" + getSpecialties(x) + "]" +
                    " | Статус аккаунта: " + x.getAccount().getAccountStatus());
    }

    private String getSpecialties(Customer customer) {
        return customer.getSpecialties().stream()
                .map(Specialty::getName)
                .map(x -> ((Specialty)customer.getSpecialties().toArray()[0]).getName().equals(x) ? x : ", " + x)
                .reduce("", String::concat);
    }

    private void update() {
        System.out.println("--Выберите покупателя для замены--");
        getAll();
        long choice = sc.nextLong();
        customerController.update(
                new Customer(addSpecialties(),updateAccountById(choice)),choice);
        main();
    }

    private Account updateAccountById(long choice) {
        System.out.println("Введите новое имя: ");
        Account account = accountController.getById(choice);
        account.setName(sc.next());
        return accountController.update(account,choice);
    }

    private void delete() {
        System.out.println("Выберите покупателя для удаления:");
        getAll();
        customerController.delete(sc.nextLong());
        main();
    }

    private void back() {
        new View().main();
    }
}
