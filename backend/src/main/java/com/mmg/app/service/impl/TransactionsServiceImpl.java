package com.mmg.app.service.impl;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.dto.TransactionDto;
import com.mmg.app.model.BankAccount;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.model.Transactions;
import com.mmg.app.model.User;
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
        transaction.setIsPlanned(transactionRequest.isPlanned()); // Set isPlanned
        transaction.setPlannedAmount(transactionRequest.getPlannedAmount());

        System.out.println("isPlanned: " + transactionRequest.isPlanned());

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
}
