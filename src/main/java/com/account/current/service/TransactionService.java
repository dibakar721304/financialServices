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
import org.springframework.util.CollectionUtils;

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
     * @date 15/05/2023
     * This method creates account for existing customer with initial credit amount.
     * If an initial credit amount is other than zero, a new transaction will be created for the account created
     * @param transactionType
     * @param currentAccount
     * @throws
     * @return Transaction object.
     */
    @Transactional
    public Transaction createTransaction(String transactionType, CurrentAccount currentAccount)
            throws AccountNotFoundException {
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
                .description("New transaction for the account is successful")
                .build();
        transactionRepository.save(transaction);
        transactions.add(transaction);
        currentAccount.setTransactionList(transactions);
        log.debug("A transaction has been saved for account number {}", currentAccount.getAccountNumber());
        return transaction;
    }
    /**
     * @auther anant dibakar
     * @date 17/05/2023
     * This method updates the account with requested transaction.
     * @param transactionType
     * @param currentAccount
     * @param transactionAmount
     * @throws
     * @return An account object.
     */
    public CurrentAccount updateCurrentAccountWithTransaction(
            CurrentAccount currentAccount, BigDecimal transactionAmount, String transactionType) {
        log.debug("A new request for updating account with new transaction");
        BigDecimal balance = currentAccount.getBalance();

        if (transactionType.equals(TransactionType.WITHDRAWAL)) {

            if (transactionAmount.compareTo(balance) > 0) {
                log.error("Balance is not sufficient to withdraw");
                throw new InsufficientFundException("Balance is not enough");
            } else {
                balance = balance.subtract(transactionAmount);
                log.debug("Remaining balance after withdrawal {}", balance);
            }
        } else if (transactionType.equals(TransactionType.DEPOSIT)) {
            balance = balance.add(transactionAmount);
            log.debug("Remaining balance after deposit {}", balance);
        }
        currentAccount.setBalance(balance);
        transaction = Transaction.builder()
                .date(LocalDateTime.now())
                .transactionType(transactionType)
                .accountId(currentAccount.getId())
                .amount(transactionAmount)
                .description("Account has been update with new transaction")
                .build();
        transactionRepository.save(transaction);
        updateTransactionList(currentAccount, transaction);
        currentAccountRepository.save(currentAccount);
        return currentAccount;
    }

    private void updateTransactionList(CurrentAccount currentAccount, Transaction transaction) {
        if (CollectionUtils.isEmpty(currentAccount.getTransactionList())) {
            transactions = new ArrayList<>();
            transactions.add(transaction);
            currentAccount.setTransactionList(transactions);
        } else {
            currentAccount.getTransactionList().add(transaction);
        }
    }
}
