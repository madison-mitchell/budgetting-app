package com.mmg.app.service.impl;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.dto.TransactionDto;
import com.mmg.app.model.*;
import com.mmg.app.repository.BankAccountRepository;
import com.mmg.app.repository.CategoryParentChildRelationsRepository;
import com.mmg.app.repository.TransactionsRepository;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CategoryParentChildRelationsRepository categoryRepository;

    @Override
    public Transactions createTransaction(TransactionDto transactionRequest) {
        System.out.println("Creating transaction with request: " + transactionRequest);

        System.out.println("User ID: " + transactionRequest.getUserId());
        System.out.println("Account ID: " + transactionRequest.getAccountId());
        System.out.println("Category ID: " + transactionRequest.getCategoryId());

        User user = userRepository.findById(transactionRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User found: " + user.getId());

        BankAccount bankAccount = bankAccountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));
        System.out.println("BankAccount found: " + bankAccount.getId());

        CategoryParentChildRelations category = categoryRepository.findById(transactionRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        System.out.println("Category found: " + category.getId());

        Transactions transaction = new Transactions();
        transaction.setUser(user);
        transaction.setAccountId(bankAccount);
        transaction.setCategoryId(category);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setTimeOfTransaction(transactionRequest.getTimeOfTransaction());
        transaction.setNotes(transactionRequest.getNotes());
        transaction.setMerchant(transactionRequest.getMerchant());
        transaction.setRecurring(transactionRequest.isRecurring());
        transaction.setFrequency(transactionRequest.getFrequency());
        transaction.setIncluded(transactionRequest.isIncluded());
        transaction.setReviewed(transactionRequest.isReviewed());
        transaction.setType(transactionRequest.getType());
        transaction.setIsPlanned(transactionRequest.isPlanned());
        transaction.setPlannedAmount(transactionRequest.getPlannedAmount());

        if (transactionRequest.getSplits() != null && !transactionRequest.getSplits().isEmpty()) {
            List<TransactionSplit> splits = transactionRequest.getSplits().stream().map(splitDto -> {
                TransactionSplit split = new TransactionSplit();
                split.setCategoryId(categoryRepository.findById(splitDto.getCategoryId()).orElseThrow());
                split.setAmount(splitDto.getAmount());
                split.setTransaction(transaction);
                return split;
            }).collect(Collectors.toList());
            transaction.setSplits(splits);
            transaction.setHasSplit(true);
        }

        return transactionsRepository.save(transaction);
    }

    @Override
    public Transactions saveTransaction(Transactions transaction) {
        return transactionsRepository.save(transaction);
    }

    @Override
    public Transactions updateTransaction(Long id, TransactionDto transactionDto) {
        Transactions existingTransaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        User user = userRepository.findById(transactionDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        BankAccount bankAccount = bankAccountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));
        CategoryParentChildRelations category = categoryRepository.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingTransaction.setUser(user);
        existingTransaction.setAccountId(bankAccount);
        existingTransaction.setCategoryId(category);
        existingTransaction.setAmount(transactionDto.getAmount());
        existingTransaction.setDescription(transactionDto.getDescription());
        existingTransaction.setTimeOfTransaction(transactionDto.getTimeOfTransaction());
        existingTransaction.setNotes(transactionDto.getNotes());
        existingTransaction.setMerchant(transactionDto.getMerchant());
        existingTransaction.setRecurring(transactionDto.isRecurring());
        existingTransaction.setFrequency(transactionDto.getFrequency());
        existingTransaction.setIncluded(transactionDto.isIncluded());
        existingTransaction.setReviewed(transactionDto.isReviewed());
        existingTransaction.setType(transactionDto.getType());
        existingTransaction.setIsPlanned(transactionDto.isPlanned());
        existingTransaction.setPlannedAmount(transactionDto.getPlannedAmount());
        existingTransaction.setAccountBalance(transactionDto.getAccountBalance());

        if (transactionDto.getSplits() != null) {
            List<TransactionSplit> splits = transactionDto.getSplits().stream()
                    .map(splitDto -> {
                        TransactionSplit split = new TransactionSplit();
                        split.setId(splitDto.getId());
                        split.setTransaction(existingTransaction);
                        split.setCategoryId(categoryRepository.findById(splitDto.getCategoryId()).orElseThrow());
                        split.setAmount(splitDto.getAmount());
                        return split;
                    }).collect(Collectors.toList());
            existingTransaction.setSplits(splits);
        }

        return transactionsRepository.save(existingTransaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionsRepository.deleteById(id);
    }

    @Override
    public Transactions getTransactionById(Long id) {
        return transactionsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transactions> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    @Override
    public List<Transactions> findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return transactionsRepository.findByUserId(user.getId());
    }

    @Override
    public List<CategoryTotalDto> calculateCategoryTotalsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        Long userId = user.getId();
        List<CategoryTotalDto> categoryTotals = transactionsRepository.findCategoryTotalsByUserId(userId);

        // Calculate parent category totals based on child categories
        Map<Long, Double> parentCategoryBudgets = new HashMap<>();
        for (CategoryTotalDto dto : categoryTotals) {
            //parentCategoryBudgets.put(dto.getParentCategoryId(), parentCategoryBudgets.getOrDefault(dto.getParentCategoryId(), 0.0) + dto.getBudget());
        }
        return categoryTotals;
    }

    @Override
    public List<Transactions> findTransactionsByAccountIdAndUsername(Long accountId, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return transactionsRepository.findTransactionsByAccountIdAndUserId(accountId, user.getId());
    }
}
