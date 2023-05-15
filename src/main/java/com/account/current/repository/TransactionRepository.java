package com.account.current.repository;

import com.account.current.model.dao.CurrentAccount;
import com.account.current.model.dao.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "select * from transaction t,  where customer_id =?1", nativeQuery = true)
    List<CurrentAccount> findByCustomerId(Long customerId);

    @Query(value = "select * from transaction t,  where account_id =?1", nativeQuery = true)
    List<Transaction> findByAccountId(Long accountId);
}
