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
}
