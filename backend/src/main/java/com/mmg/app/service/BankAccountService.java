package com.mmg.app.service;

import com.mmg.app.model.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount saveBankAccount(BankAccount bankAccount);
    BankAccount updateBankAccount(BankAccount bankAccount);
    void deleteBankAccount(Long id);
    BankAccount getBankAccountById(Long id);
    List<BankAccount> getAllBankAccounts();

    List<BankAccount> findByUsername(String currentUsername);
}
