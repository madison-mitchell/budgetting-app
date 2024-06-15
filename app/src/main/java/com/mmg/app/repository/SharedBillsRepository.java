package com.mmg.app.repository;

import com.mmg.app.model.SharedBills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedBillsRepository extends JpaRepository<SharedBills, Long> {
}
