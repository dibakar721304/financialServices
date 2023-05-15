package com.account.current.model.dto;

import com.account.current.model.dao.CurrentAccount;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime date;
    private Enum transactionType;
    private String description;
    private CurrentAccount currentAccount;
}
