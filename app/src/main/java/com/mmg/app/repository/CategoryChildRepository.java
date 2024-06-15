package com.mmg.app.repository;

import com.mmg.app.model.CategoryChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryChildRepository extends JpaRepository<CategoryChild, Long> {
}
