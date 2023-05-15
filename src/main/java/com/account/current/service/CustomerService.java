package com.account.current.service;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dto.CustomerDto;
import com.account.current.repository.CustomerRepository;
import com.account.current.util.ModelMapper;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDto getCustomerDetails(Customer customer) {
        List<CurrentAccount> currentAccountList = accountService.getAccountDetails(customer);
        return ModelMapper.mapToCustomerDto(customer, currentAccountList);
    }
}
