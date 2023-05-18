package com.account.current.service;

import com.account.current.exception.CustomerNotFoundException;
import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dto.CustomerDto;
import com.account.current.repository.CustomerRepository;
import com.account.current.util.ModelMapper;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionService.class);

    private final CustomerRepository customerRepository;

    private final AccountService accountService;

    public CustomerService(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    public CustomerDto getCustomerDetails(Long customerId) {
        log.debug("A request has been created for fetching customer details with id {}", customerId);
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        List<CurrentAccount> currentAccountList = accountService.getAccountDetails(customerId);
        return ModelMapper.mapToCustomerDto(customer, currentAccountList);
    }

    public Customer createCustomer(String name, String email, String surName) {
        Customer customer =
                Customer.builder().name(name).surName(surName).email(email).build();
        customerRepository.save(customer);
        return customer;
    }
}
