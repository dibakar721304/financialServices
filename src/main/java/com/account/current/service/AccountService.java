package com.account.current.service;

import com.account.current.exception.AccountNotFoundException;
import com.account.current.exception.CustomerNotFoundException;
import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dto.CurrentAccountDto;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.repository.CustomerRepository;
import com.account.current.util.ModelMapper;
import com.account.current.util.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    private final CurrentAccountRepository currentAccountRepository;

    @Autowired
    private TransactionService transactionService;

    private CustomerRepository customerRepository;

    public AccountService(CurrentAccountRepository currentAccountRepository, CustomerRepository customerRepository) {
        this.currentAccountRepository = currentAccountRepository;
        this.customerRepository = customerRepository;
    }
    /**
     * @auther anant dibakar
     * @date 15/005/2023
     * This method creates account for existing customer with initial credit amount.
     * If an initial credit amount is other than zero, a new transaction will be created for the account created
     * @param customerId
     * @param initialCredit
     * @throws
     * @return An account object.
     */
    public CurrentAccountDto createAccountForCustomer(Long customerId, BigDecimal initialCredit)
            throws CustomerNotFoundException {
        log.debug("A request sent with customer {} and initial credit amount {}", customerId, initialCredit);
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("customer not found"));
        CurrentAccount currentAccount = CurrentAccount.builder()
                .accountNumber(UUID.randomUUID().toString().replace("-", ""))
                .customer(customer)
                .balance(initialCredit)
                .createDate(LocalDateTime.now())
                .description("New account has been created")
                .build();
        currentAccountRepository.save(currentAccount);
        log.debug(
                "An account has been created for customer id {} and account number {} ",
                customerId,
                currentAccount.getAccountNumber());
        if (initialCredit.compareTo(BigDecimal.ZERO) != 0) {
            log.info("Starting new transaction for account id {}", currentAccount.getId());
            transactionService.createTransaction(TransactionType.INITIAL_DEPOSIT, currentAccount);
        }
        CurrentAccountDto currentAccountDto = ModelMapper.mapToCurrentAccountDto(currentAccount);
        return currentAccountDto;
    }

    public List<CurrentAccount> getAccountDetails(Long customerId) {
        List<CurrentAccount> currentAccountList = currentAccountRepository.findByCustomerId(customerId);
        if (currentAccountList.isEmpty()) {
            log.info("No account has been found for customer id {}", customerId);
            currentAccountList = Collections.emptyList();
        }
        return currentAccountList;
    }

    public CurrentAccountDto updateAccountForCustomer(
            Long accountId, BigDecimal transactionAmount, String transactionType) {
        Optional<CurrentAccount> currentAccountOptional = Optional.ofNullable(currentAccountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found")));
        CurrentAccount currentAccount = currentAccountOptional.get();
        currentAccount = transactionService.updateCurrentAccountWithTransaction(
                currentAccount, transactionAmount, transactionType);
        return ModelMapper.mapToCurrentAccountDto(currentAccount);
    }
}
