package com.mmg.app.repository;

import com.mmg.app.model.CategoryParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryParentRepository extends JpaRepository<CategoryParent, Long> {
}
