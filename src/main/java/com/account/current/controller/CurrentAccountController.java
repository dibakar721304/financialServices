package com.account.current.controller;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.repository.CustomerRepository;
import com.account.current.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("account")
public class CurrentAccountController {
    private final AccountService accountService;

    private final CustomerRepository customerRepository;

    public CurrentAccountController(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }
/*
• The API will expose an endpoint which accepts the user information (customerID, initialCredit).
• Once the endpoint is called, a new account will be opened connected to the user whose ID is customerID.
 */
    @PostMapping("/customers/{customerId}/{initialCredit}")
    public ResponseEntity<CurrentAccount> createAccountForCustomer(@PathVariable Long customerId, @PathVariable BigDecimal initialCredit) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            CurrentAccount currentAccount = accountService.createAccountForCustomer(customer, initialCredit);
            return ResponseEntity.ok(currentAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
    

