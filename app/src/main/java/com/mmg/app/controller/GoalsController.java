package com.mmg.app.controller;

import com.mmg.app.model.Goals;
import com.mmg.app.service.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/goals")
public class GoalsController {

    @Autowired
    private GoalsService goalsService;

    @PostMapping
    public Goals createGoal(@RequestBody Goals goal) {
        return goalsService.saveGoal(goal);
    }

    @PutMapping("/{id}")
    public Goals updateGoal(@PathVariable Long id, @RequestBody Goals goal) {
        goal.setId(id);
        return goalsService.updateGoal(goal);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Long id) {
        goalsService.deleteGoal(id);
    }

    @GetMapping("/{id}")
    public Goals getGoalById(@PathVariable Long id) {
        return goalsService.getGoalById(id);
    }

    @GetMapping
    public List<Goals> getAllGoals() {
        return goalsService.getAllGoals();
    }
}
