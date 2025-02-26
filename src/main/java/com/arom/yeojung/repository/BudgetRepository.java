package com.arom.yeojung.repository;

import com.arom.yeojung.object.Budget;
import com.arom.yeojung.object.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByBudgetType(BudgetType budgetType);
    List<Budget> findBySubPlan_SubPlanId(Long subPlanId);
}
