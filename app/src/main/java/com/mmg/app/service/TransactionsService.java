package com.mmg.app.service;

import com.mmg.app.model.Transactions;

import java.util.List;

public interface TransactionsService {
    Transactions saveTransaction(Transactions transaction);
    Transactions updateTransaction(Transactions transaction);
    void deleteTransaction(Long id);
    Transactions getTransactionById(Long id);
    List<Transactions> getAllTransactions();
}
