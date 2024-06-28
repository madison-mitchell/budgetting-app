package com.mmg.app.service.impl;

import com.mmg.app.dto.CategoryBudgetDto;
import com.mmg.app.model.CategoryBudget;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.repository.CategoryBudgetRepository;
import com.mmg.app.repository.CategoryParentChildRelationsRepository;
import com.mmg.app.repository.TransactionsRepository;
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

    @Override
    public List<CategoryBudget> getBudgetsByMonthAndYear(int month, int year) {
        return categoryBudgetRepository.findByYearAndMonth(year, month);
    }

    @Override
    public List<CategoryBudget> getCategoryBudgetByRelationIdAndMonthYear(Long categoryId, int month, int year) {
        return categoryBudgetRepository.findByCategoryIdAndYearAndMonth(categoryId, year, month);
    }

    @Transactional
    @Override
    public CategoryBudget saveOrUpdateBudget(CategoryBudgetDto categoryBudgetDto) {
        CategoryParentChildRelations categoryRelation = categoryParentChildRelationsRepository.findById(categoryBudgetDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category relation not found"));

        List<CategoryBudget> categoryBudgets = categoryBudgetRepository.findByCategoryIdAndYearAndMonth(categoryRelation.getId(), categoryBudgetDto.getYear(), categoryBudgetDto.getMonth());
        CategoryBudget categoryBudget;
        if (!categoryBudgets.isEmpty()) {
            categoryBudget = categoryBudgets.get(0);
        } else {
            categoryBudget = new CategoryBudget();
            categoryBudget.setCategory(categoryRelation);
        }

        categoryBudget.setYear(categoryBudgetDto.getYear());
        categoryBudget.setMonth(categoryBudgetDto.getMonth());
        categoryBudget.setBudgetAmount(categoryBudgetDto.getBudgetAmount());

        // Update the amount spent based on transactions
        Double totalAmountSpent = transactionsRepository.calculateTotalAmountSpent(
                categoryRelation.getId(), categoryBudgetDto.getYear(), categoryBudgetDto.getMonth());
        categoryBudget.setAmountSpent(totalAmountSpent != null ? totalAmountSpent : 0.0);

        return categoryBudgetRepository.save(categoryBudget);
    }
}
