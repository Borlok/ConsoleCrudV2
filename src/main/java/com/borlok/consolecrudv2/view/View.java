package com.borlok.consolecrudv2.view;

import java.util.Scanner;

public class View {
    private final AccountView accountView;
    private final CustomerView customerView;
    private final SpecialtyView specialtyView;

    public View() {
        accountView = new AccountView();
        customerView = new CustomerView();
        specialtyView = new SpecialtyView();
    }

    public void main () {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("\n--Меню--\n" +
                    "Добро пожаловать, выберите вариант");
            while (true) {
                System.out.println(
                        "1: Покупатели\n" +
                        "2: Аккаунт\n" +
                        "3: Специальность\n" +
                        "4: Выход");
                switch (sc.nextInt()) {
                    case 1 -> customerView.main();
                    case 2 -> accountView.main();
                    case 3 -> specialtyView.main();
                    case 4 -> System.exit(0);
                    default -> System.out.println("Такой команды не существует");
                }
            }
        }
    }

}
