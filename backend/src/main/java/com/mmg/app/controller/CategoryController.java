package com.mmg.app.controller;

import com.mmg.app.model.CategoryParent;
import com.mmg.app.model.CategoryChild;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
