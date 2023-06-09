package com.account.current.service;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.account.current.exception.InsufficientFundException;
import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dao.Transaction;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @Mock
    private CurrentAccountRepository currentAccountRepositoryMock;

    @Test
    void test_createNewTransaction_given_customerId_and_initialCredit_zero() {
        CurrentAccount currentAccount = CurrentAccount.builder()
                .accountNumber("12345")
                .transactionList(Collections.EMPTY_LIST)
                .createDate(LocalDateTime.now())
                .description("Testing create transaction with zero initial credit")
                .balance(BigDecimal.ZERO)
                .customer(Customer.builder().build())
                .build();

        Transaction transaction = transactionService.createTransaction("INITIAL_DEPOSIT", currentAccount);
        assertEquals("balance", BigDecimal.ZERO, transaction.getAmount());
        assertEquals("transactionType", "INITIAL_DEPOSIT", transaction.getTransactionType());
        assertEquals("transactionList", "INITIAL_DEPOSIT", transaction.getTransactionType());
    }

    @Test
    void test_createNewTransaction_given_customerId_and_initialCredit_other_than_zero() {
        CurrentAccount currentAccount = CurrentAccount.builder()
                .accountNumber("12345")
                .transactionList(Collections.EMPTY_LIST)
                .createDate(LocalDateTime.now())
                .description("Testing create transaction with initial credit as 10")
                .balance(BigDecimal.TEN)
                .customer(Customer.builder().build())
                .build();
        Transaction transaction = transactionService.createTransaction("INITIAL_DEPOSIT", currentAccount);
        assertEquals("balance", BigDecimal.TEN, transaction.getAmount());
        assertEquals("transactionType", "INITIAL_DEPOSIT", transaction.getTransactionType());
        assertEquals(
                "transactionAmount", currentAccount.getTransactionList().get(0).getAmount(), transaction.getAmount());
        assertEquals("accountId", currentAccount.getId(), transaction.getAccountId());
    }

    @Test
    void test_updateCurrentAccountWithTransaction_given_account_and_withdrwawal_amount() {
        CurrentAccount currentAccount = CurrentAccount.builder()
                .id(1l)
                .accountNumber("12345")
                .transactionList(Collections.EMPTY_LIST)
                .createDate(LocalDateTime.now())
                .description("Testing withdrwal transaction with account balance as 10")
                .balance(BigDecimal.TEN)
                .customer(Customer.builder().build())
                .build();
        CurrentAccount currentAccountActual = transactionService.updateCurrentAccountWithTransaction(
                currentAccount, BigDecimal.valueOf(4), "WITHDRAWAL");
        assertEquals("balance", BigDecimal.valueOf(6), currentAccountActual.getBalance());
    }

    @Test
    void test_updateCurrentAccountWithTransaction_given_account_and_deposit_amount() {
        CurrentAccount currentAccount = CurrentAccount.builder()
                .id(1l)
                .accountNumber("12345")
                .transactionList(Collections.EMPTY_LIST)
                .createDate(LocalDateTime.now())
                .description("Testing deposit transaction with  account balance as 10")
                .balance(BigDecimal.TEN)
                .customer(Customer.builder().build())
                .build();
        CurrentAccount currentAccountActual = transactionService.updateCurrentAccountWithTransaction(
                currentAccount, BigDecimal.valueOf(4), "DEPOSIT");
        assertEquals("balance", BigDecimal.valueOf(14), currentAccountActual.getBalance());
    }

    @Test
    void test_updateCurrentAccountWithTransaction_given_account_and_withrawal_more_than_balance() {
        CurrentAccount currentAccount = CurrentAccount.builder()
                .id(1l)
                .accountNumber("12345")
                .transactionList(Collections.EMPTY_LIST)
                .createDate(LocalDateTime.now())
                .description("Testing create transaction with initial credit as 10")
                .balance(BigDecimal.TEN)
                .customer(Customer.builder().build())
                .build();
        Assertions.assertThrows(InsufficientFundException.class, () -> {
            transactionService.updateCurrentAccountWithTransaction(
                    currentAccount, BigDecimal.valueOf(15), "WITHDRAWAL");
        });
    }
}
