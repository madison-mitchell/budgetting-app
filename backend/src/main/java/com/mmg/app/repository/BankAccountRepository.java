package com.mmg.app.repository;

import com.mmg.app.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUserUsername(String username);

    List<BankAccount> findByUserId(Long id);
}
