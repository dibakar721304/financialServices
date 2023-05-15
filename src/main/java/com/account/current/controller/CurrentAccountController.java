package com.account.current.controller;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dto.CurrentAccountDto;
import com.account.current.repository.CustomerRepository;
import com.account.current.service.AccountService;
import com.account.current.util.ModelMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class CurrentAccountController {
    private final AccountService accountService;

    private final CustomerRepository customerRepository;

    public CurrentAccountController(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    @PostMapping("/currentAccount/{customerId}/{initialCredit}")
    public ResponseEntity<CurrentAccountDto> createAccountForCustomer(
            @PathVariable Long customerId, @PathVariable BigDecimal initialCredit) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            CurrentAccount currentAccount = accountService.createAccountForCustomer(customer, initialCredit);

            CurrentAccountDto currentAccountDto = ModelMapper.mapToCurrentAccountDto(currentAccount);
            return ResponseEntity.ok(currentAccountDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
