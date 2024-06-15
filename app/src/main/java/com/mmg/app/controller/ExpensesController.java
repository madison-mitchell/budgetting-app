package com.mmg.app.controller;

import com.mmg.app.model.Expenses;
import com.mmg.app.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    @PostMapping
    public Expenses createExpense(@RequestBody Expenses expense) {
        return expensesService.saveExpense(expense);
    }

    @PutMapping("/{id}")
    public Expenses updateExpense(@PathVariable Long id, @RequestBody Expenses expense) {
        expense.setId(id);
        return expensesService.updateExpense(expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expensesService.deleteExpense(id);
    }

    @GetMapping("/{id}")
    public Expenses getExpenseById(@PathVariable Long id) {
        return expensesService.getExpenseById(id);
    }

    @GetMapping
    public List<Expenses> getAllExpenses() {
        return expensesService.getAllExpenses();
    }
}
