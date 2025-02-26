package com.arom.yeojung.repository;

import com.arom.yeojung.object.SubPlan;
import com.arom.yeojung.object.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubPlanRepository extends JpaRepository<SubPlan, Long> {
  List<SubPlan> findByDailyPlan(DailyPlan dailyPlan);
}
