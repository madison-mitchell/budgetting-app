package com.mmg.app.service;

import com.mmg.app.model.Goals;

import java.util.List;

public interface GoalsService {
    Goals saveGoal(Goals goal);
    Goals updateGoal(Goals goal);
    void deleteGoal(Long id);
    Goals getGoalById(Long id);
    List<Goals> getAllGoals();
}
