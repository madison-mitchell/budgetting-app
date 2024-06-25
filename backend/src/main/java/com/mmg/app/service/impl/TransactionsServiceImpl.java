package com.mmg.app.service.impl;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.dto.TransactionDto;
import com.mmg.app.dto.TransactionSplitDto;
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
        User user = userRepository.findById(transactionRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        BankAccount bankAccount = bankAccountRepository.findById(transactionRequest.getBankAccountId())
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));
        CategoryParentChildRelations category = categoryRepository.findById(transactionRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transactions transaction = new Transactions();
        transaction.setUser(user);
        transaction.setBankAccountId(bankAccount);
        transaction.setCategoryId(category);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setTimeOfTransaction(transactionRequest.getTimeOfTransaction());
        transaction.setNotes(transactionRequest.getNotes());
        transaction.setMerchant(transactionRequest.getMerchant());
        transaction.setRecurring(transactionRequest.isRecurring());
        transaction.setFrequency(transactionRequest.getFrequency());
        transaction.setIncluded(transactionRequest.isIncluded());
        transaction.setReviewed(transactionRequest.getReviewed());
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
            transaction.setHasSplit(true); // Update has_split indicator
        }

        return transactionsRepository.save(transaction);
    }

    @Override
    public Transactions saveTransaction(Transactions transaction) {
        return transactionsRepository.save(transaction);
    }

    @Override
    public Transactions updateTransaction(Transactions transaction) {
        return transactionsRepository.save(transaction);
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
            parentCategoryBudgets.put(dto.getParentCategoryId(), parentCategoryBudgets.getOrDefault(dto.getParentCategoryId(), 0.0) + dto.getBudget());
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

//    public TransactionSplitDto convertToDto(TransactionSplit split) {
//        TransactionSplitDto dto = new TransactionSplitDto();
//        dto.setId(split.getId());
//        dto.setTransactionId(split.getTransaction().getId());
//        dto.setCategoryId(split.getCategoryId().getId());
//        dto.setAmount(split.getAmount());
//        return dto;
//    }
//
//    public TransactionDto convertToDto(Transactions transaction) {
//        TransactionDto dto = new TransactionDto();
//        dto.setId(transaction.getId());
//        dto.setAmount(transaction.getAmount());
//        dto.setDescription(transaction.getDescription());
//        dto.setTimeOfTransaction(transaction.getTimeOfTransaction());
//        dto.setSplits(transaction.getSplits().stream().map(this::convertToDto).collect(Collectors.toList()));
//        return dto;
//    }
}
