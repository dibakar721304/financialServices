package com.account.current.controller;

import com.account.current.exception.CustomerNotFoundException;
import com.account.current.model.dto.CustomerDto;
import com.account.current.repository.CustomerRepository;
import com.account.current.service.AccountService;
import com.account.current.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }
    /**
     * @auther anant dibakar
     * @date 15/05/2023
     * End point to get details for existing customer.
     * @param customerId
     * @throws CustomerNotFoundException
     * @return A customer object.
     */
    @ApiOperation("Create a new account with initial credit")
    @ApiResponses(
            value = {
                @ApiResponse(code = 202, message = "User created"),
                @ApiResponse(code = 400, message = "Invalid request"),
                @ApiResponse(code = 404, message = "Not found")
            })
    @GetMapping("/details/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerDetails(@PathVariable Long customerId) {
        log.debug("A request sent with customer id {} ", customerId);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerDetails(customerId));
    }
}
