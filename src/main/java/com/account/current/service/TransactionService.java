package com.account.current.service;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Transaction;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Transactional
    public Transaction createTransaction(Transaction transaction, String accountNumber) {
        CurrentAccount bankAccount = currentAccountRepository.findByAccountNumber(accountNumber);
        List<Transaction> transactions = bankAccount.getTransactionList();
        if(null!=transactions) {
            transactions.add(transaction);
            bankAccount.setTransactionList(transactions);
        }
        BigDecimal balance = bankAccount.getBalance();
        if (transaction.getAmount().compareTo(balance)==1)
            throw new RuntimeException("Balance is not enough");
        else if (transaction.getAmount().compareTo(balance)==-1) {
            balance.subtract(transaction.getAmount());
            transaction.setTransactionType("Withdrawal");
        }
        else if(transaction.getTransactionType().equals("deposit"))
        {
            balance.add(transaction.getAmount());
        }
        bankAccount.setBalance(balance);
        return transactionRepository.save(transaction);
    }

    public Transaction transactionGetAPI(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
