package com.borlok.consolecrudv2.repository.io;

import com.borlok.consolecrudv2.model.Account;
import com.borlok.consolecrudv2.model.AccountStatus;
import com.borlok.consolecrudv2.model.Customer;
import com.borlok.consolecrudv2.model.Specialty;
import com.borlok.consolecrudv2.repository.AccountRepository;
import com.borlok.consolecrudv2.repository.CustomerRepository;
import com.borlok.consolecrudv2.repository.SpecialtyRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerRepositoryImpl implements CustomerRepository {
    private static CustomerRepository customerRepository;
    private final SpecialtyRepository specialtyRepository;
    private final AccountRepository accountRepository;
    private final String FILE_PATH = "./src/main/resources/files/customers.txt";
    private final Path PATH = Paths.get(FILE_PATH);

    private CustomerRepositoryImpl() {
        specialtyRepository = SpecialtyRepositoryImpl.getInstance();
        accountRepository = AccountRepositoryImpl.getInstance();
    }

    public static CustomerRepository getInstance() {
        if (customerRepository == null)
            customerRepository = new CustomerRepositoryImpl();
        return customerRepository;
    }

    @Override
    public Customer create(Customer customer) {
        List<Customer> allCustomersList = getAll();
        allCustomersList.add(customer);
        saveCollectionOfCustomerToFile(allCustomersList);
        return customer;
    }

    private void saveCollectionOfCustomerToFile(List<Customer> allCustomersList) {
        try (FileWriter writer = new FileWriter(PATH.toFile())) {
            for (Customer customer : allCustomersList)
                writer.write(customer.getId() + "," +
                        customer.getAccount().getName() + "," +
                        customer.getAccount().getAccountStatus() +
                        getSpecialties(customer.getSpecialties()) + "\n");
        } catch (IOException e) {
            System.err.println("Проблема при записи аккаунта в файл: " + e);
        }
    }

    private String getSpecialties(Set<Specialty> specialties) {
        return specialties.stream().map(Specialty::getId)
                .map(x -> "," + x)
                .reduce("",String::concat);
    }

    @Override
    public Customer getById(Long id) {
        return getAll().stream().filter(x -> x.getId() == Math.toIntExact(id))
                .findFirst().orElse(isNotInRepository());

    }

    private Customer isNotInRepository () {
        return new Customer(new HashSet<>(), ((AccountRepositoryImpl)accountRepository).isNotInRepository());
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> returnedList = new ArrayList<>();
        try {
            List<String> customersList = Files.readAllLines(PATH);
            returnedList = customersList.stream()
                    .map(x -> x.split(","))
                    .map(x -> {
                        Account account = accountRepository.getById(Long.parseLong(x[0]));
                        Customer customerToReturnedList = new Customer(
                            Arrays.stream(x).skip(3)
                                    .map(z -> specialtyRepository.getById(Long.parseLong(z)))
                                    .collect(Collectors.toSet())
                                ,account);
                    customerToReturnedList.setId(Integer.parseInt(x[0]));
                    return customerToReturnedList;})
                    .filter(x ->!(x.getAccount().getAccountStatus().equals(AccountStatus.DELETED)))
                    .collect(Collectors.toList());
            saveCollectionOfCustomerToFile(returnedList);
        } catch (IOException e) {
            System.err.println("Ошибка при получении специальностей из файла: " + e);
        }
        return returnedList;
    }

    @Override
    public Customer update(Customer customer, Long id) {
        List<Customer> allSpecialtiesList = getAll();

        Account account = accountRepository.getById(id);
        account.setName(customer.getAccount().getName());
        account.setAccountStatus(customer.getAccount().getAccountStatus());
        accountRepository.update(account,id);

        allSpecialtiesList.set(allSpecialtiesList.indexOf(allSpecialtiesList.stream()
                .filter(x -> x.getId() == id).findFirst()
                .orElse(isNotInRepository())),customer);
        saveCollectionOfCustomerToFile(allSpecialtiesList);
        return customer;
    }

    @Override
    public void delete(Long id) {
        List<Customer> allSpecialtiesList = getAll();
        allSpecialtiesList.remove(allSpecialtiesList.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(isNotInRepository()));
        accountRepository.delete(id);
        saveCollectionOfCustomerToFile(allSpecialtiesList);
    }
}
