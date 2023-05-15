package com.account.current.model.dto;

import com.account.current.model.dao.Customer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CurrentAccountDto {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Customer customer;

    private String description;
    private LocalDateTime createDate;

    private List<TransactionDto> transactionList;
}
