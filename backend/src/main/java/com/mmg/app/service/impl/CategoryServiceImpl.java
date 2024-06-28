package com.mmg.app.service.impl;

import com.mmg.app.model.CategoryParent;
import com.mmg.app.model.CategoryChild;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.repository.CategoryParentRepository;
import com.mmg.app.repository.CategoryChildRepository;
import com.mmg.app.repository.CategoryParentChildRelationsRepository;
import com.mmg.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryParentRepository categoryParentRepository;

    @Autowired
    private CategoryChildRepository categoryChildRepository;

    @Autowired
    private CategoryParentChildRelationsRepository categoryParentChildRelationsRepository;

    @Override
    public CategoryParent saveCategoryParent(CategoryParent categoryParent) {
        return categoryParentRepository.save(categoryParent);
    }

    @Override
    public CategoryChild saveCategoryChild(CategoryChild categoryChild) {
        return categoryChildRepository.save(categoryChild);
    }

    @Override
    public CategoryParentChildRelations saveCategoryParentChildRelations(CategoryParentChildRelations categoryParentChildRelations) {
        return categoryParentChildRelationsRepository.save(categoryParentChildRelations);
    }

    @Override
    public List<CategoryParent> getAllCategoryParents() {
        return categoryParentRepository.findAll();
    }

    @Override
    public List<CategoryChild> getAllCategoryChildren() {
        return categoryChildRepository.findAll();
    }

    @Override
    public List<CategoryParentChildRelations> getAllCategoryParentChildRelations() {
        return categoryParentChildRelationsRepository.findAll();
    }

    @Override
    public CategoryParent getCategoryParentById(Long id) {
        return categoryParentRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryChild getCategoryChildById(Long id) {
        return categoryChildRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryParentChildRelations getCategoryParentChildRelationsById(Long id) {
        return categoryParentChildRelationsRepository.findById(id).orElse(null);
    }

    public boolean setBudget(Long relationId, Double newBudget) {
        Optional<CategoryParentChildRelations> optionalRelation = categoryParentChildRelationsRepository.findById(relationId);

        if (optionalRelation.isPresent()) {
            CategoryParentChildRelations relation = optionalRelation.get();
            categoryParentChildRelationsRepository.save(relation);
            return true;
        }

        return false;
    }
}
