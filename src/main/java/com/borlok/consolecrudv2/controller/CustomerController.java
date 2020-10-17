package com.borlok.consolecrudv2.controller;

import com.borlok.consolecrudv2.model.Account;
import com.borlok.consolecrudv2.model.Customer;
import com.borlok.consolecrudv2.model.Specialty;
import com.borlok.consolecrudv2.repository.CustomerRepository;
import com.borlok.consolecrudv2.repository.io.CustomerRepositoryImpl;

import java.util.List;
import java.util.Set;

public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController() {
        customerRepository = CustomerRepositoryImpl.getInstance();
    }

    public Customer create(Set<Specialty> specialties, Account account) {
        Customer customer = new Customer(specialties, account);
        List<Customer> customerList = customerRepository.getAll();
        if (customerList.isEmpty())
            customer.setId(1);
        else
            customer.setId(customerList.get(customerList.size() - 1).getId() + 1);
        return customerRepository.create(customer);
    }

    public List<Customer> read() {
        return customerRepository.getAll();
    }

    public Customer update (Customer customer, Long id) {
        customer.setId(id.intValue());
        customer.getAccount().setAccountStatus(customer.getAccount().getAccountStatus());
        return customerRepository.update(customer, id);
    }

    public void delete(Long id) {
        customerRepository.delete(id);
    }
}
