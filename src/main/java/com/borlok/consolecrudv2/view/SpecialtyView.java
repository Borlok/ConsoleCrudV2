package com.borlok.consolecrudv2.view;

import com.borlok.consolecrudv2.controller.AccountController;
import com.borlok.consolecrudv2.controller.CustomerController;
import com.borlok.consolecrudv2.controller.SpecialtyController;
import com.borlok.consolecrudv2.model.Specialty;

import java.util.Scanner;

public class SpecialtyView {
    private final CustomerController customerController;
    private final SpecialtyController specialityController;
    private final AccountController accountController;
    private Scanner sc;

    public SpecialtyView() {
        customerController = new CustomerController();
        specialityController = new SpecialtyController();
        accountController = new AccountController();
    }

    public void main() {
        try {
            sc = new Scanner(System.in);
            int choice;
            System.out.println("\n--Специальности--\n" +
                    "Выберите действие:\n" +
                    "1: Добавить специальность\n" +
                    "2: Посмотреть специальности\n" +
                    "3: Редактировать специальность\n" +
                    "4: Удалить специальность\n" +
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
        System.out.println("Введите имя специальности:");
        specialityController.create(sc.next());
        main();
    }

    private void get() {
        getAll();
        System.out.println("Введите любой символ для продолжения...");
        sc.next();
        main();
    }

    private void getAll() {
        for (Specialty x : specialityController.read())
            System.out.println("id: " + x.getId() +
                    " | Наименование: " + x.getName());
    }

    private void update() {
        System.out.println("--Выберите специальность для замены--");
        getAll();
        long choice = sc.nextLong();
        System.out.println("Введите новую специальность: ");
        specialityController.update(sc.next(),choice);
        main();
    }

    private void delete() {
        System.out.println("Выберите специальность для удаления:");
        getAll();
        specialityController.delete(sc.nextLong());
        main();
    }

    private void back() {
        new View().main();
    }
}
