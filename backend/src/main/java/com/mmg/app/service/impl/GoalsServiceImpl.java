package com.mmg.app.service.impl;

import com.mmg.app.model.Goals;
import com.mmg.app.repository.GoalsRepository;
import com.mmg.app.service.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalsServiceImpl implements GoalsService {

    @Autowired
    private GoalsRepository goalsRepository;

    @Override
    public Goals saveGoal(Goals goal) {
        return goalsRepository.save(goal);
    }

    @Override
    public Goals updateGoal(Goals goal) {
        return goalsRepository.save(goal);
    }

    @Override
    public void deleteGoal(Long id) {
        goalsRepository.deleteById(id);
    }

    @Override
    public Goals getGoalById(Long id) {
        return goalsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Goals> getAllGoals() {
        return goalsRepository.findAll();
    }
}