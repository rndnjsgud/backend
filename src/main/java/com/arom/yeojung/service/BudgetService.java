package com.arom.yeojung.service;

import com.arom.yeojung.object.Budget;
import com.arom.yeojung.object.BudgetType;
import com.arom.yeojung.object.SubPlan;
import com.arom.yeojung.object.dto.BudgetRequestDTO;
import com.arom.yeojung.object.dto.BudgetResponseDTO;
import com.arom.yeojung.repository.BudgetRepository;
import com.arom.yeojung.repository.SubPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final SubPlanRepository subPlanRepository;

    // 생성
    public BudgetResponseDTO createBudget(BudgetRequestDTO dto) {
        // 요청에 담긴 subPlanId를 통해 SubPlan 엔티티 조회
        SubPlan subPlan = subPlanRepository.findById(dto.getSubPlanId())
                .orElseThrow(() -> new RuntimeException("SubPlan not found with id: " + dto.getSubPlanId()));

        Budget budget = Budget.builder()
                .budgetCategory(dto.getBudgetCategory())
                .budgetName(dto.getBudgetName())
                .budgetAmount(dto.getBudgetAmount())
                .budgetType(dto.getBudgetType())
                .subPlan(subPlan)
                .build();

        Budget saved = budgetRepository.save(budget);
        return mapToResponseDTO(saved);
    }

    // 단건 조회
    public BudgetResponseDTO getBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
        return mapToResponseDTO(budget);
    }

    // 전체 조회
    public List<BudgetResponseDTO> getAllBudgets() {
        return budgetRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // budgetType에 따른 조회
    public List<BudgetResponseDTO> getBudgetsByBudgetType(BudgetType budgetType){
        return budgetRepository.findByBudgetType(budgetType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 수정
    public BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO dto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));

        budget.setBudgetCategory(dto.getBudgetCategory());
        budget.setBudgetName(dto.getBudgetName());
        budget.setBudgetAmount(dto.getBudgetAmount());
        budget.setBudgetType(dto.getBudgetType());

        // SubPlan이 변경되었을 경우
        if (!budget.getSubPlan().getSubPlanId().equals(dto.getSubPlanId())) {
            SubPlan subPlan = subPlanRepository.findById(dto.getSubPlanId())
                    .orElseThrow(() -> new RuntimeException("SubPlan not found with id: " + dto.getSubPlanId()));
            budget.setSubPlan(subPlan);
        }
        Budget updated = budgetRepository.save(budget);
        return mapToResponseDTO(updated);
    }

    // 삭제
    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new RuntimeException("Budget not found with id: " + id);
        }
        budgetRepository.deleteById(id);
    }

    // Budget 엔티티를 BudgetResponseDTO로 매핑
    private BudgetResponseDTO mapToResponseDTO(Budget budget) {
        return BudgetResponseDTO.builder()
                .budgetId(budget.getBudgetId())
                .budgetCategory(budget.getBudgetCategory())
                .budgetName(budget.getBudgetName())
                .budgetAmount(budget.getBudgetAmount())
                .budgetType(budget.getBudgetType())
                .subPlanId(budget.getSubPlan().getSubPlanId())
                .build();
    }
}
