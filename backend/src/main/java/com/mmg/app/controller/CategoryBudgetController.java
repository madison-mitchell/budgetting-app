package com.mmg.app.controller;

import com.mmg.app.dto.CategoryBudgetDto;
import com.mmg.app.model.CategoryBudget;
import com.mmg.app.service.CategoryBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-budgets")
public class CategoryBudgetController {

    @Autowired
    private CategoryBudgetService categoryBudgetService;

    @PostMapping
    public CategoryBudget createOrUpdateBudget(@RequestBody CategoryBudgetDto categoryBudgetDto) {
        return categoryBudgetService.saveOrUpdateBudget(categoryBudgetDto);
    }

    @GetMapping
    public List<CategoryBudget> getBudgetsByMonthAndYear(@RequestParam int month, @RequestParam int year) {
        return categoryBudgetService.getBudgetsByMonthAndYear(month, year);
    }
}
