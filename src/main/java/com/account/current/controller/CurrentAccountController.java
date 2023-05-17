package com.account.current.controller;

import com.account.current.model.dto.CurrentAccountDto;
import com.account.current.repository.CustomerRepository;
import com.account.current.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class CurrentAccountController {
    private final AccountService accountService;

    private final CustomerRepository customerRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CurrentAccountController.class);

    public CurrentAccountController(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    /**
     * @auther anant dibakar
     * @date 15/05/2023
     * End point to create account for existing customer.
     * @param customerId
     * @param initialCredit
     * @throws
     * @return An account object.
     */
    @ApiOperation("Create a new account with initial credit")
    @ApiResponses(
            value = {
                @ApiResponse(code = 202, message = "User created"),
                @ApiResponse(code = 400, message = "Invalid request"),
                @ApiResponse(code = 404, message = "Not found")
            })
    @PostMapping(value = "/currentAccount/{customerId}/{initialCredit}")
    public ResponseEntity<CurrentAccountDto> createAccountForCustomer(
            @PathVariable Long customerId, @PathVariable BigDecimal initialCredit) {
        log.debug("A request sent with customer {} and initial credit amount {}", customerId, initialCredit);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccountForCustomer(customerId, initialCredit));
    }
    /**
     * @auther anant dibakar
     * @date 17/05/2023
     * End point to update account for existing customer.
     * @param accountId,transactionAmount and transactionType
     * @throws
     * @return An account object.
     */
    @ApiOperation("Create a new account with initial credit")
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Account has been update successfully"),
                @ApiResponse(code = 400, message = "Invalid request"),
                @ApiResponse(code = 404, message = "Account not found")
            })
    @PutMapping(value = "/currentAccount/{accountId}/{transactionAmount}/{transactionType}")
    public ResponseEntity<CurrentAccountDto> updateAccountForCustomer(
            @PathVariable Long accountId,
            @PathVariable BigDecimal transactionAmount,
            @PathVariable String transactionType) {
        log.debug(
                "A request sent with accountId {} and initial credit amount {}",
                accountId,
                transactionAmount,
                transactionType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.updateAccountForCustomer(accountId, transactionAmount, transactionType));
    }
}
