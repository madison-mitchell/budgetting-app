package com.mmg.app.service;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.dto.TransactionDto;
import com.mmg.app.model.Transactions;

import java.util.List;

public interface TransactionsService {
    Transactions createTransaction(TransactionDto transactionRequest);

    Transactions saveTransaction(Transactions transaction);

    Transactions updateTransaction(Long id, TransactionDto transactionDto);

    void deleteTransaction(Long id);

    Transactions getTransactionById(Long id);

    List<Transactions> getAllTransactions();

    List<Transactions> findByUsername(String username);

    List<CategoryTotalDto> calculateCategoryTotalsByUsername(String username);

    List<Transactions> findTransactionsByAccountIdAndUsername(Long accountId, String username);
}
