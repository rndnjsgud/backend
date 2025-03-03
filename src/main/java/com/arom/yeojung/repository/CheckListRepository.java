package com.arom.yeojung.repository;

import com.arom.yeojung.object.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    // TotalPlan 엔티티의 planId를 기준으로 조회
    List<CheckList> findByTotalPlan_TotalPlanId(Long totalPlanId);
}
