package com.account.current.service;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.util.TransactionType;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountService {
    private final CurrentAccountRepository currentAccountRepository;

    @Autowired
    private TransactionService transactionService;

    public AccountService(CurrentAccountRepository currentAccountRepository) {
        this.currentAccountRepository = currentAccountRepository;
    }

    public CurrentAccount createAccountForCustomer(Customer customer, BigDecimal initialCredit) {

        CurrentAccount currentAccount = CurrentAccount.builder()
                .accountNumber(UUID.randomUUID().toString().replace("-", ""))
                .customer(customer)
                .balance(initialCredit)
                .description("New account has been created")
                .build();
        currentAccountRepository.save(currentAccount);
        if (initialCredit.compareTo(BigDecimal.ZERO) != 0) {
            transactionService.createTransaction(TransactionType.INITIAL_DEPOSIT, currentAccount.getAccountNumber());
        }
        return currentAccount;
    }

    public List<CurrentAccount> getAccountDetails(Customer customer) {
        List<CurrentAccount> currentAccountList = currentAccountRepository.findByCustomerId(customer.getId());
        if (currentAccountList.isEmpty()) {
            currentAccountList = Collections.emptyList();
        }
        return currentAccountList;
    }
}
