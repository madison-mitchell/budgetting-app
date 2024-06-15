package com.mmg.app.service;

import com.mmg.app.model.CategoryParent;
import com.mmg.app.model.CategoryChild;
import com.mmg.app.model.CategoryParentChildRelations;

import java.util.List;

public interface CategoryService {
    CategoryParent saveCategoryParent(CategoryParent categoryParent);
    CategoryChild saveCategoryChild(CategoryChild categoryChild);
    CategoryParentChildRelations saveCategoryParentChildRelations(CategoryParentChildRelations categoryParentChildRelations);
    List<CategoryParent> getAllCategoryParents();
    List<CategoryChild> getAllCategoryChildren();
    List<CategoryParentChildRelations> getAllCategoryParentChildRelations();
}
