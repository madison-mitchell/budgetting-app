package com.mmg.app.service.impl;

import com.mmg.app.model.BankAccount;
import com.mmg.app.model.User;
import com.mmg.app.repository.BankAccountRepository;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return null;
    }

    public BankAccount updateBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public void deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }

    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id).orElse(null);
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public List<BankAccount> findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return bankAccountRepository.findByUserId(user.getId());
    }
}