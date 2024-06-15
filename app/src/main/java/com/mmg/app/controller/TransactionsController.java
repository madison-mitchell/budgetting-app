package com.mmg.app.controller;

import com.mmg.app.model.Transactions;
import com.mmg.app.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @PostMapping
    public Transactions createTransaction(@RequestBody Transactions transaction) {
        return transactionsService.saveTransaction(transaction);
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
    public List<Transactions> getAllTransactions() {
        return transactionsService.getAllTransactions();
    }
}
