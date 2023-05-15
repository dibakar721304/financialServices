package com.account.current.util;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Customer;
import com.account.current.model.dao.Transaction;
import com.account.current.model.dto.CurrentAccountDto;
import com.account.current.model.dto.CustomerDto;
import com.account.current.model.dto.TransactionDto;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModelMapper {

    public CustomerDto mapToCustomerDto(Customer customer, List<CurrentAccount> currentAccountList) {
        return CustomerDto.builder()
                .name(customer.getName())
                .surName(customer.getSurName())
                .currentAccountDtoList(populateAccountDto(currentAccountList))
                .build();
    }

    private List<CurrentAccountDto> populateAccountDto(List<CurrentAccount> currentAccountList) {
        return currentAccountList.stream()
                .map(currentAccount -> mapToCurrentAccountDto(currentAccount))
                .collect(Collectors.toList());
    }

    public CurrentAccountDto mapToCurrentAccountDto(CurrentAccount currentAccount) {
        return CurrentAccountDto.builder()
                .accountNumber(currentAccount.getAccountNumber())
                .id(currentAccount.getId())
                .balance(currentAccount.getBalance())
                .customer(currentAccount.getCustomer())
                .createDate(currentAccount.getCreateDate())
                .description(currentAccount.getDescription())
                .transactionList(populateTransactionDtoList(currentAccount.getTransactionList()))
                .build();
    }

    private List<TransactionDto> populateTransactionDtoList(List<Transaction> transactionList) {
        if (null == transactionList) {
            transactionList = Collections.EMPTY_LIST;
        }
        return transactionList.stream()
                .map(transaction -> mapToTransactionDto(transaction))
                .collect(Collectors.toList());
    }

    private TransactionDto mapToTransactionDto(Transaction transation) {
        return TransactionDto.builder()
                .transactionType(transation.getTransactionType())
                .id(transation.getId())
                .amount(transation.getAmount())
                .date(transation.getDate())
                .description(transation.getDescription())
                .build();
    }
}
