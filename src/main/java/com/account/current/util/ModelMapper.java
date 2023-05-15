package com.account.current.util;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dao.Transaction;
import com.account.current.model.dto.CurrentAccountDto;
import com.account.current.model.dto.CustomerDto;
import com.account.current.model.dto.TransactionDto;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    public static CustomerDto mapToCustomerDto(Customer customer, List<CurrentAccount> currentAccountList) {
        return CustomerDto.builder()
                .name(customer.getName())
                .surName(customer.getSurName())
                .currentAccountDtoList(populateAccountDto(currentAccountList))
                .build();
    }

    private static List<CurrentAccountDto> populateAccountDto(List<CurrentAccount> currentAccountList) {
        return currentAccountList.stream()
                .map(d -> ModelMapper.mapToCurrentAccountDto(d))
                .collect(Collectors.toList());
    }

    public static CurrentAccountDto mapToCurrentAccountDto(CurrentAccount currentAccount) {
        return CurrentAccountDto.builder()
                .accountNumber(currentAccount.getAccountNumber())
                .id(currentAccount.getId())
                .balance(currentAccount.getBalance())
                .customer(currentAccount.getCustomer())
                .description(currentAccount.getDescription())
                .transactionList(populateTransactionDtoList(currentAccount.getTransactionList()))
                .build();
    }

    private static List<TransactionDto> populateTransactionDtoList(List<Transaction> transactionList) {
        return transactionList.stream()
                .map(d -> ModelMapper.mapToTransactionDto(d))
                .collect(Collectors.toList());
    }

    private static TransactionDto mapToTransactionDto(Transaction transation) {
        return TransactionDto.builder()
                .transactionType(transation.getTransactionType())
                .id(transation.getId())
                .amount(transation.getAmount())
                .date(transation.getDate())
                .description(transation.getDescription())
                .build();
    }
}
