package com.mmg.app.repository;

import com.mmg.app.model.CategoryBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {

    @Query("SELECT cb FROM CategoryBudget cb WHERE cb.year = :year AND cb.month = :month")
    List<CategoryBudget> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT cb FROM CategoryBudget cb WHERE cb.category.id = :categoryId AND cb.year = :year AND cb.month = :month")
    List<CategoryBudget> findByCategoryIdAndYearAndMonth(@Param("categoryId") Long categoryId, @Param("year") int year, @Param("month") int month);
}
