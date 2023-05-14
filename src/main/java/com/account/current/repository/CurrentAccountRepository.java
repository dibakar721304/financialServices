package com.account.current.repository;

import com.account.current.model.dao.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, Long> {

    @Query(value="select * from current_account where account_number =?1",
    nativeQuery = true)
    CurrentAccount findByAccountNumber(String accountNumber);
}

