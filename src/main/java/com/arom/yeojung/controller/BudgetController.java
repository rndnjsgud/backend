package com.arom.yeojung.controller;

import com.arom.yeojung.object.BudgetType;
import com.arom.yeojung.object.dto.BudgetRequestDTO;
import com.arom.yeojung.object.dto.BudgetResponseDTO;
import com.arom.yeojung.object.dto.CheckListResponseDTO;
import com.arom.yeojung.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    // 예산 생성
    @PostMapping
    public ResponseEntity<BudgetResponseDTO> createBudget(@RequestBody BudgetRequestDTO dto) {
        BudgetResponseDTO response = budgetService.createBudget(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> getBudget(@PathVariable Long id) {
        BudgetResponseDTO response = budgetService.getBudget(id);
        return ResponseEntity.ok(response);
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> getAllBudgets() {
        List<BudgetResponseDTO> responseList = budgetService.getAllBudgets();
        return ResponseEntity.ok(responseList);
    }

    //예산 타입에 따른 조회
    @GetMapping("/byBudgetType")
    public ResponseEntity<List<BudgetResponseDTO>> getBudgetsByBudgetType(BudgetType budgetType){
        List<BudgetResponseDTO> responseList = budgetService.getBudgetsByBudgetType(budgetType);
        return ResponseEntity.ok(responseList);
    }

    //subPlanId에 따른 조회
    @GetMapping("/{subPlanId}")
    public ResponseEntity<List<BudgetResponseDTO>> getBudgetListsBySubPlanId(@PathVariable Long subPlanId){
        List<BudgetResponseDTO> responseLists = budgetService.getBudgetListsBySubPlanId(subPlanId);
        return ResponseEntity.ok(responseLists);
    }

    // 예산 수정
    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> updateBudget(@PathVariable Long id,
                                                          @RequestBody BudgetRequestDTO dto) {
        BudgetResponseDTO response = budgetService.updateBudget(id, dto);
        return ResponseEntity.ok(response);
    }

    // 예산 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
