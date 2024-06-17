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
    @Query("SELECT new com.mmg.app.dto.CategoryTotalDto(c.parent.id, c.parent.name, c.child.id, c.child.name, SUM(t.amount)) " +
            "FROM Transactions t JOIN t.category c " +
            "WHERE t.user.id = :userId " +
            "GROUP BY c.parent.id, c.parent.name, c.child.id, c.child.name")
    List<CategoryTotalDto> findCategoryTotalsByUserId(@Param("userId") Long userId);

    List<Transactions> findByUserId(Long id);
}
