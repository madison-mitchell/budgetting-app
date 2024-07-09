package com.mmg.app.repository;

import com.mmg.app.model.CategoryParentChildRelations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryParentChildRelationsRepository extends JpaRepository<CategoryParentChildRelations, Long> {
    CategoryParentChildRelations findByChildCategoryName(String childCategoryName);
}
