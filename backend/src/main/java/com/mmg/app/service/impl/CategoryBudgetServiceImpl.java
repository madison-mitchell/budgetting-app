package com.mmg.app.service.impl;

import com.mmg.app.dto.CategoryBudgetDto;
import com.mmg.app.model.CategoryBudget;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.repository.CategoryBudgetRepository;
import com.mmg.app.repository.CategoryParentChildRelationsRepository;
import com.mmg.app.repository.TransactionsRepository;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.service.CategoryBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryBudgetServiceImpl implements CategoryBudgetService {

    @Autowired
    private CategoryBudgetRepository categoryBudgetRepository;

    @Autowired
    private CategoryParentChildRelationsRepository categoryParentChildRelationsRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CategoryBudget> getBudgetsByMonthAndYear(int month, int year, Long userId) {
        return categoryBudgetRepository.findByYearAndMonthAndUserId(year, month, userId);
    }

    @Override
    public CategoryBudget getCategoryBudgetByRelationIdAndMonthYear(Long categoryId, int month, int year, Long userId) {
        return categoryBudgetRepository.findByCategoryIdAndYearAndMonthAndUserId(categoryId, year, month, userId)
                .orElse(null);
    }

    @Transactional
    @Override
    public CategoryBudget saveOrUpdateBudget(CategoryBudgetDto categoryBudgetDto) {
        CategoryParentChildRelations categoryRelation = categoryParentChildRelationsRepository.findById(categoryBudgetDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category relation not found"));

        CategoryBudget categoryBudget = categoryBudgetRepository.findByCategoryIdAndYearAndMonthAndUserId(
                        categoryBudgetDto.getCategoryId(), categoryBudgetDto.getYear(), categoryBudgetDto.getMonth(), categoryBudgetDto.getUserId())
                .orElse(new CategoryBudget());

        categoryBudget.setCategory(categoryRelation);
        categoryBudget.setUser(userRepository.findById(categoryBudgetDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found")));
        categoryBudget.setYear(categoryBudgetDto.getYear());
        categoryBudget.setMonth(categoryBudgetDto.getMonth());
        categoryBudget.setBudgetAmount(categoryBudgetDto.getBudgetAmount());

        Double totalAmountSpent = transactionsRepository.calculateTotalAmountSpent(
                categoryBudgetDto.getCategoryId(), categoryBudgetDto.getYear(), categoryBudgetDto.getMonth());
        categoryBudget.setAmountSpent(totalAmountSpent != null ? totalAmountSpent : 0.0);

        return categoryBudgetRepository.save(categoryBudget);
    }
}
