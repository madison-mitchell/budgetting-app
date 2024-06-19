package com.mmg.app.controller;

import com.mmg.app.model.CategoryParent;
import com.mmg.app.model.CategoryChild;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/parent")
    public CategoryParent createCategoryParent(@RequestBody CategoryParent categoryParent) {
        return categoryService.saveCategoryParent(categoryParent);
    }

    @PostMapping("/child")
    public CategoryChild createCategoryChild(@RequestBody CategoryChild categoryChild) {
        return categoryService.saveCategoryChild(categoryChild);
    }

    @PostMapping("/relation")
    public CategoryParentChildRelations createCategoryParentChildRelations(@RequestBody CategoryParentChildRelations categoryParentChildRelations) {
        return categoryService.saveCategoryParentChildRelations(categoryParentChildRelations);
    }

    @GetMapping("/parent")
    public List<CategoryParent> getAllCategoryParents() {
        return categoryService.getAllCategoryParents();
    }

    @GetMapping("/child")
    public List<CategoryChild> getAllCategoryChildren() {
        return categoryService.getAllCategoryChildren();
    }

    @GetMapping("/relation")
    public List<CategoryParentChildRelations> getAllCategoryParentChildRelations() {
        return categoryService.getAllCategoryParentChildRelations();
    }

//    @PutMapping("/relation/{relationId}/budget")
//    public ResponseEntity<?> updateBudget(@PathVariable Long relationId, @RequestBody Map<String, Double> budget) {
//        Double newBudget = budget.get("budget");
//        if (newBudget != null) {
//            // Call service to update budget
//            boolean updated = categoryService.setBudget(relationId, newBudget);
//            if (updated) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid budget value");
//        }
//    }

    @PutMapping("/relation/{id}/budget")
    public ResponseEntity<?> updateBudget(@PathVariable Long id, @RequestBody Map<String, Double> budgetMap) {
        double newBudget = budgetMap.get("budget");
        categoryService.setBudget(id, newBudget);
        return ResponseEntity.ok().build();
    }
}
