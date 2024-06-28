package com.mmg.app.service;

import com.mmg.app.dto.CategoryBudgetDto;
import com.mmg.app.model.CategoryBudget;

import java.util.List;

public interface CategoryBudgetService {
    List<CategoryBudget> getBudgetsByMonthAndYear(int month, int year);
    List<CategoryBudget> getCategoryBudgetByRelationIdAndMonthYear(Long categoryId, int month, int year);
    CategoryBudget saveOrUpdateBudget(CategoryBudgetDto categoryBudgetDto);
}
