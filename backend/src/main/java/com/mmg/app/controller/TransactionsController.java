package com.mmg.app.controller;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.dto.TransactionDto;
import com.mmg.app.dto.TransactionSplitDto;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<Transactions> updateTransaction(@PathVariable Long id, @RequestBody TransactionDto transactionDto) {
        try {
            System.out.println("Incoming TransactionDto: " + transactionDto);
            Transactions updatedTransaction = transactionsService.updateTransaction(id, transactionDto);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convert Transactions to TransactionDto
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setUserId(transaction.getUser().getId());
        transactionDto.setAccountId(transaction.getAccountId().getId());
        transactionDto.setCategoryId(transaction.getCategoryId().getId());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setTimeOfTransaction(transaction.getTimeOfTransaction());
        transactionDto.setNotes(transaction.getNotes());
        transactionDto.setMerchant(transaction.getMerchant());
        transactionDto.setRecurring(transaction.isRecurring());
        transactionDto.setFrequency(transaction.getFrequency());
        transactionDto.setIncluded(transaction.isIncluded());
        transactionDto.setReviewed(reviewed);
        transactionDto.setType(transaction.getType());
        transactionDto.setIsPlanned(transaction.isPlanned());
        transactionDto.setPlannedAmount(transaction.getPlannedAmount());
        transactionDto.setSplits(transaction.getSplits().stream().map(split -> {
            TransactionSplitDto splitDto = new TransactionSplitDto();
            splitDto.setId(split.getId());
            splitDto.setTransactionId(split.getTransaction().getId());
            splitDto.setCategoryId(split.getCategoryId().getId());
            splitDto.setAmount(split.getAmount());
            // Add other fields if needed
            return splitDto;
        }).collect(Collectors.toList()));

        Transactions updatedTransaction = transactionsService.updateTransaction(id, transactionDto);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }



}
