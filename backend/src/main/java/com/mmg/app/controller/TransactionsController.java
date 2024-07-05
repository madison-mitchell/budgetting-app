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

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping
    public ResponseEntity<Transactions> createTransaction(@RequestBody TransactionDto transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("Incoming transaction request: " + transactionRequest);

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

    @PutMapping("/{id}/reviewed")
    public ResponseEntity<Transactions> updateTransactionReviewed(@PathVariable Long id, @RequestBody Boolean reviewed) {
        Transactions transaction = transactionsService.getTransactionById(id);
        transaction.setReviewed(reviewed);
        Transactions updatedTransaction = transactionsService.updateTransaction(transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }


}
