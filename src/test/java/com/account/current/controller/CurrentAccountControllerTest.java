package com.account.current.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.current.exception.CustomerNotFoundException;
import com.account.current.model.dao.Customer;
import com.account.current.model.dto.CurrentAccountDto;
import com.account.current.repository.CurrentAccountRepository;
import com.account.current.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CurrentAccountControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private CurrentAccountController currentAccountControllerMock;

    @Mock
    private AccountService accountServiceMock;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(currentAccountControllerMock).build();
    }

    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Test
    public void test_createAccountForCustomer() throws Exception {
        CurrentAccountDto currentAccountDto = CurrentAccountDto.builder()
                .id(1L)
                .createDate(LocalDateTime.now())
                .accountNumber("12345")
                .customer(new Customer())
                .build();
        Mockito.when(accountServiceMock.createAccountForCustomer(1L, BigDecimal.ZERO))
                .thenReturn(currentAccountDto);

        mockMvc.perform(post("/account/currentAccount/1/0")).andExpect(status().isCreated());
    }

    @Test
    public void test_createAccountForCustomerNotExist() throws Exception {
        CurrentAccountDto currentAccountDto = CurrentAccountDto.builder()
                .id(1L)
                .createDate(LocalDateTime.now())
                .accountNumber("12345")
                .customer(new Customer())
                .build();
        Mockito.when(accountServiceMock.createAccountForCustomer(1L, BigDecimal.ZERO))
                .thenReturn(currentAccountDto);
        //
        //        mockMvc.perform(post("/account/currentAccount/1/0"))
        //                .andExpect(status().isCreated());

        mockMvc.perform(post("/account/currentAccount/0"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerNotFoundException))
                .andExpect(result -> assertEquals(
                        "customer does not exist", result.getResolvedException().getMessage()));
    }
}
