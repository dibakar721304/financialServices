package com.account.current.controller;

import com.account.current.model.dao.Customer;
import com.account.current.model.dto.CustomerDto;
import com.account.current.repository.CustomerRepository;
import com.account.current.service.CustomerService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @GetMapping("/details/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerDetails(@PathVariable Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return ResponseEntity.ok(customerService.getCustomerDetails(customer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
