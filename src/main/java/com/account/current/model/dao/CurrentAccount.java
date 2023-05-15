package com.account.current.model.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "current_account")
public class CurrentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal balance;

    @ManyToOne
    private Customer customer;

    private String description;

    @OneToMany
    private List<Transaction> transactionList;
}
