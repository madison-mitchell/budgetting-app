package com.mmg.app.service.impl;

import com.mmg.app.model.Expenses;
import com.mmg.app.model.User;
import com.mmg.app.repository.ExpensesRepository;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesServiceImpl implements ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Expenses saveExpense(Expenses expense) {
        return expensesRepository.save(expense);
    }

    @Override
    public Expenses updateExpense(Expenses expense) {
        return expensesRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        expensesRepository.deleteById(id);
    }

    @Override
    public Expenses getExpenseById(Long id) {
        return expensesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Expenses> getAllExpenses() {
        return expensesRepository.findAll();
    }

    @Override
    public List<Expenses> findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return expensesRepository.findByUserId(user.getId());
    }
}
