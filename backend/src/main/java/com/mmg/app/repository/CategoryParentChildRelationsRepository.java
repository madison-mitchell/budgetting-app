package com.mmg.app.repository;

import com.mmg.app.model.CategoryParentChildRelations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryParentChildRelationsRepository extends JpaRepository<CategoryParentChildRelations, Long> {
    CategoryParentChildRelations findByChildCategoryName(String childCategoryName);
    List<CategoryParentChildRelations> findAllByOrderByChildCategoryNameAsc();

}
