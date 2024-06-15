package com.mmg.app.service;

import com.mmg.app.model.Income;

import java.util.List;

public interface IncomeService {
    Income saveIncome(Income income);
    Income updateIncome(Income income);
    void deleteIncome(Long id);
    Income getIncomeById(Long id);
    List<Income> getAllIncomes();
}
