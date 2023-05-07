package com.account.current.repository;

import com.account.current.model.dao.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, Long> {}

