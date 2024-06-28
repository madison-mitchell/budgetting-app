package com.mmg.app.repository;

import com.mmg.app.model.CategoryBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {

    @Query("SELECT cb FROM CategoryBudget cb WHERE cb.year = :year AND cb.month = :month AND cb.user.id = :userId")
    List<CategoryBudget> findByYearAndMonthAndUserId(@Param("year") int year, @Param("month") int month, @Param("userId") Long userId);

    @Query("SELECT cb FROM CategoryBudget cb WHERE cb.category.id = :categoryId AND cb.year = :year AND cb.month = :month AND cb.user.id = :userId")
    Optional<CategoryBudget> findByCategoryIdAndYearAndMonthAndUserId(@Param("categoryId") Long categoryId, @Param("year") int year, @Param("month") int month, @Param("userId") Long userId);
}
