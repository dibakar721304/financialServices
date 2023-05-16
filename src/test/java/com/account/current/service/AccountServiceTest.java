package com.account.current.service;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.repository.CustomerRepository;
import com.account.current.util.ModelMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Mock
    private CurrentAccountRepository currentAccountRepositoryMock;

    @Test
    public void test_createAccountForCustomer() {
        var currentAccount = CurrentAccount.builder()
                .id(1L)
                .accountNumber("12345")
                .transactionList(Collections.EMPTY_LIST)
                .balance(BigDecimal.ZERO)
                .build();
        var customer = Customer.builder().id(1L).name("TEST").build();
        Mockito.when(customerRepositoryMock.findById(1L)).thenReturn(Optional.of(customer));
        var x = ModelMapper.mapToCurrentAccountDto(currentAccount);

        var currentAccountDto = accountService.createAccountForCustomer(1L, BigDecimal.ZERO);

        // Assertions
        assertEquals("balance", BigDecimal.ZERO, currentAccountDto.getBalance());
        assertEquals("customerId", 1L, currentAccountDto.getCustomer().getId());
    }
}
