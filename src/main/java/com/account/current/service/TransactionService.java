package com.account.current.service;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Transaction;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.repository.TransactionRepository;
import com.account.current.util.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    CurrentAccountRepository currentAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private List<Transaction> transactions;
    private Transaction transaction;

    @Transactional
    public Transaction createTransaction(TransactionType transactionType, String accountNumber) {
        CurrentAccount currentAccount = currentAccountRepository.findByAccountNumber(accountNumber);
        transactions = currentAccount.getTransactionList();
        if (null != transactions) {
            return updateTransactionForCurrentAccount(currentAccount, transaction, transactionType);
        }
        transactions = new ArrayList<>();
        transaction = Transaction.builder()
                .transactionType(transactionType)
                .accountId(currentAccount.getId())
                .date(LocalDateTime.now())
                .amount(currentAccount.getBalance())
                .description("First transaction with initial deposit")
                .build();
        transactions.add(transaction);
        currentAccount.setTransactionList(transactions);
        transaction = transactionRepository.save(transaction);
        currentAccountRepository.save(currentAccount);
        return transaction;
    }

    public Transaction updateTransactionForCurrentAccount(
            CurrentAccount currentAccount, Transaction transaction, TransactionType transactionType) {
        BigDecimal balance = currentAccount.getBalance();

        if (transactionType.equals(TransactionType.WITHDRWAL)) {

            if (transaction.getAmount().compareTo(balance) > 0) {
                throw new RuntimeException("Balance is not enough");
            } else {
                balance.subtract(transaction.getAmount());
            }
        } else if (transactionType.equals(TransactionType.DEPOSIT)) {
            balance.add(transaction.getAmount());
        }
        currentAccount.setBalance(balance);
        transaction.setDate(LocalDateTime.now());
        transaction.setAccountId(currentAccount.getId());
        transaction.setDescription("Transaction has been added to account");
        currentAccountRepository.save(currentAccount);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionDetails(Long account_id) {
        return transactionRepository.findByAccountId(account_id);
    }
}
