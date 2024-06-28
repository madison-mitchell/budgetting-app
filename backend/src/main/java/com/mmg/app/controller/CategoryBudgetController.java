package com.mmg.app.controller;

import com.mmg.app.dto.CategoryBudgetDto;
import com.mmg.app.model.CategoryBudget;
import com.mmg.app.model.User;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.service.CategoryBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-budgets")
public class CategoryBudgetController {

    @Autowired
    private CategoryBudgetService categoryBudgetService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public CategoryBudget createOrUpdateBudget(@RequestBody CategoryBudgetDto categoryBudgetDto) {
        return categoryBudgetService.saveOrUpdateBudget(categoryBudgetDto);
    }

    @GetMapping
    public List<CategoryBudget> getBudgetsByMonthAndYear(@RequestParam int month, @RequestParam int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return categoryBudgetService.getBudgetsByMonthAndYear(month, year, user.getId());
    }
}
