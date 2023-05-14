package com.account.current.service;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dao.Transaction;
import com.account.current.repository.CurrentAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

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

        CurrentAccount currentAccount=CurrentAccount.builder().accountNumber(UUID.randomUUID().toString().replace("-", "")).customer(customer)
                .balance(BigDecimal.ZERO).build();
        currentAccountRepository.save(currentAccount);
        if(initialCredit.compareTo(BigDecimal.ZERO)!=0)
        {
            transactionService.createTransaction(Transaction.builder().amount(initialCredit).build(), currentAccount.getAccountNumber());
        }
        return currentAccount;
    }
}
