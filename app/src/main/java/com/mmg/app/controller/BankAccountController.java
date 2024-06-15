package com.mmg.app.controller;

import com.mmg.app.model.BankAccount;
import com.mmg.app.repository.BankAccountRepository;
import com.mmg.app.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @PutMapping("/{id}")
    public BankAccount updateBankAccount(@PathVariable Long id, @RequestBody BankAccount bankAccount) {
        bankAccount.setId(id);
        return bankAccountService.updateBankAccount(bankAccount);
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
    }

    @GetMapping("/{id}")
    public BankAccount getBankAccountById(@PathVariable Long id) {
        return bankAccountService.getBankAccountById(id);
    }

    @GetMapping
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/bank-accounts")
    public List<BankAccount> getUserBankAccounts(Authentication authentication) {
        String username = authentication.getName();
        // Assuming you have a method to find bank accounts by username
        return bankAccountRepository.findByUserUsername(username);
    }
}
