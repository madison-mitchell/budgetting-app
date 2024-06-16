package com.mmg.app.service;

import com.mmg.app.model.Expenses;
import java.util.List;

public interface ExpensesService {
    Expenses saveExpense(Expenses expense);
    Expenses updateExpense(Expenses expense);
    void deleteExpense(Long id);
    Expenses getExpenseById(Long id);
    List<Expenses> getAllExpenses();
    List<Expenses> findByUsername(String username);
}
