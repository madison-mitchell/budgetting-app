package com.mmg.app.controller;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.dto.TransactionDto;
import com.mmg.app.model.Transactions;
import com.mmg.app.repository.BankAccountRepository;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.service.TransactionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionsController(TransactionsService transactionsService, UserRepository userRepository,
            BankAccountRepository bankAccountRepository) {
        this.transactionsService = transactionsService;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @PostMapping
    public ResponseEntity<Transactions> createTransaction(@RequestBody TransactionDto transactionRequest) {
        Transactions newTransaction = transactionsService.createTransaction(transactionRequest);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Transactions updateTransaction(@PathVariable Long id, @RequestBody Transactions transaction) {
        transaction.setId(id);
        return transactionsService.updateTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionsService.deleteTransaction(id);
    }

    @GetMapping("/{id}")
    public Transactions getTransactionById(@PathVariable Long id) {
        return transactionsService.getTransactionById(id);
    }

    // @GetMapping
    // public List<Transactions> getAllTransactions() {
    // return transactionsService.getAllTransactions();
    // }

    @GetMapping
    public List<Transactions> getTransactionsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return transactionsService.findByUsername(currentUsername);
    }

    @GetMapping("/categories")
    public List<Transactions> getTransactionsCategoriesForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return transactionsService.findByUsername(currentUsername);
    }

    @GetMapping("/category-totals")
    public ResponseEntity<List<CategoryTotalDto>> getCategoryTotalsByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        List<CategoryTotalDto> categoryTotals = transactionsService.calculateCategoryTotalsByUsername(currentUsername);
        return new ResponseEntity<>(categoryTotals, HttpStatus.OK);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transactions>> getTransactionsByAccountId(@PathVariable Long accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        List<Transactions> transactions = transactionsService.findTransactionsByAccountIdAndUsername(accountId,
                currentUsername);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
