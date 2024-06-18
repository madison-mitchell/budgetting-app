package com.mmg.app.service.impl;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.model.Transactions;
import com.mmg.app.model.User;
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
            parentCategoryBudgets.put(dto.getParentId(), parentCategoryBudgets.getOrDefault(dto.getParentId(), 0.0) + dto.getBudget());
        }

        // Update parent category budgets in the DTOs
        for (CategoryTotalDto dto : categoryTotals) {
            dto.setBudget(parentCategoryBudgets.get(dto.getParentId()));
        }

        return categoryTotals;
    }
}
