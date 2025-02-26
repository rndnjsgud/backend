package com.arom.yeojung.repository;

import com.arom.yeojung.object.Budget;
import com.arom.yeojung.object.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByBudgetType(BudgetType budgetType);
}
