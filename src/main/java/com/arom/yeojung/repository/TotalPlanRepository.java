package com.arom.yeojung.repository;

import com.arom.yeojung.object.TotalPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalPlanRepository extends JpaRepository<TotalPlan, Long> {
}