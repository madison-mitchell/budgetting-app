package com.mmg.app.repository;

import com.mmg.app.dto.CategoryTotalDto;
import com.mmg.app.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    @Query("SELECT new com.mmg.app.dto.CategoryTotalDto(" +
            "c.id, p.id, p.name, ch.id, ch.name, SUM(t.amount), c.budget) " +
            "FROM Transactions t " +
            "JOIN t.category c " +
            "JOIN c.parentCategory p " +
            "JOIN c.childCategory ch " +
            "WHERE t.user.id = :userId " +
            "GROUP BY c.id, p.id, p.name, ch.id, ch.name, c.budget")
    List<CategoryTotalDto> findCategoryTotalsByUserId(@Param("userId") Long userId);

    List<Transactions> findByUserId(Long id);
}
