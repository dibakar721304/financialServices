package com.account.current.service;

import com.account.current.exception.AccountNotFoundException;
import com.account.current.exception.InsufficientFundException;
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
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    CurrentAccountRepository currentAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private List<Transaction> transactions;
    private Transaction transaction;

    /**
     * @auther anant dibakar
     * @date 15/005/2023
     * This method creates account for existing customer with initial credit amount.
     * If an initial credit amount is other than zero, a new transaction will be created for the account created
     * @param transactionType
     * @param accountNumber
     * @throws
     * @return Transaction object.
     */
    @Transactional
    public Transaction createTransaction(String transactionType, String accountNumber) throws AccountNotFoundException {
        CurrentAccount currentAccount = currentAccountRepository.findByAccountNumber(accountNumber);
        if (null == currentAccount) {
            log.error("account does not exist, transaction can not be created");
            throw new AccountNotFoundException("account does not exisst");
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
        log.debug("A transaction has been saved for account number {}", currentAccount.getAccountNumber());
        return transactionRepository.save(transaction);
    }

    public Transaction updateCurrentAccountWithTransaction(
            CurrentAccount currentAccount, Transaction transaction, TransactionType transactionType) {
        log.debug("A new request for updating account with new transaction");
        BigDecimal balance = currentAccount.getBalance();

        if (transactionType.equals(TransactionType.WITHDRWAL)) {

            if (transaction.getAmount().compareTo(balance) > 0) {
                log.error("Balance is not sufficient to withdraw");
                throw new InsufficientFundException("Balance is not enough");
            } else {
                balance.subtract(transaction.getAmount());
                log.debug("Remaining balance after withdrawal {}", balance);
            }
        } else if (transactionType.equals(TransactionType.DEPOSIT)) {
            balance.add(transaction.getAmount());
            log.debug("Remaining balance after deposit {}", balance);
        }
        currentAccount.setBalance(balance);
        transaction.setDate(LocalDateTime.now());
        transaction.setAccountId(currentAccount.getId());
        transaction.setDescription("Transaction has been updated to account");
        currentAccountRepository.save(currentAccount);

        return transactionRepository.save(transaction);
    }
}
