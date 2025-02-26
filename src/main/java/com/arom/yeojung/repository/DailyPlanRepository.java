package com.arom.yeojung.repository;

import com.arom.yeojung.object.DailyPlan;
import com.arom.yeojung.object.TotalPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    // 특정 Plan ID와 DayNumber로 DailyPlan 조회
    Optional<DailyPlan> findByTotalPlan_TotalPlanIdAndTripDayNumber(Long planId, int tripDayNumber);

    // 기존: TotalPlan ID로 모든 DailyPlan 조회
    List<DailyPlan> findByTotalPlan_TotalPlanId(Long planId);

  List<DailyPlan> findByTotalPlan(TotalPlan totalPlan);
}

