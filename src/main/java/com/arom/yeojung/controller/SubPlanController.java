package com.arom.yeojung.controller;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.SubPlanRequestDTO;
import com.arom.yeojung.object.dto.SubPlanResponseDTO;
import com.arom.yeojung.service.SubPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/total-plans/{totalPlanId}/daily-plans/{dailyPlanId}/sub-plans")
@RequiredArgsConstructor
public class SubPlanController {

  private final SubPlanService subPlanService;

  // 단일 SubPlan 조회
  @GetMapping("/{subPlanId}")
  public ResponseEntity<SubPlanResponseDTO> getSubPlan(@PathVariable Long totalPlanId,
      @PathVariable Long dailyPlanId,
      @PathVariable Long subPlanId) {
    SubPlanResponseDTO response = subPlanService.getSubPlanById(totalPlanId, dailyPlanId, subPlanId);
    return ResponseEntity.ok(response);
  }

  // 특정 DailyPlan에 속한 모든 SubPlan 조회
  @GetMapping
  public ResponseEntity<List<SubPlanResponseDTO>> getAllSubPlans(@PathVariable Long totalPlanId,
      @PathVariable Long dailyPlanId) {
    List<SubPlanResponseDTO> responses = subPlanService.getAllSubPlans(totalPlanId, dailyPlanId);
    return ResponseEntity.ok(responses);
  }

  // 생성
  @PostMapping
  public ResponseEntity<SubPlanResponseDTO> createSubPlan(@PathVariable Long totalPlanId,
      @PathVariable Long dailyPlanId,
      @RequestBody @Valid SubPlanRequestDTO requestDTO,
      @AuthenticationPrincipal User currentUser) {
    SubPlanResponseDTO response = subPlanService.createSubPlan(totalPlanId, dailyPlanId, requestDTO, currentUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 수정
  @PutMapping("/{subPlanId}")
  public ResponseEntity<SubPlanResponseDTO> updateSubPlan(@PathVariable Long totalPlanId,
      @PathVariable Long dailyPlanId,
      @PathVariable Long subPlanId,
      @RequestBody @Valid SubPlanRequestDTO requestDTO,
      @AuthenticationPrincipal User currentUser) {
    SubPlanResponseDTO response = subPlanService.updateSubPlan(totalPlanId, dailyPlanId, subPlanId, requestDTO, currentUser);
    return ResponseEntity.ok(response);
  }

  // 삭제
  @DeleteMapping("/{subPlanId}")
  public ResponseEntity<Void> deleteSubPlan(@PathVariable Long totalPlanId,
      @PathVariable Long dailyPlanId,
      @PathVariable Long subPlanId,
      @AuthenticationPrincipal User currentUser) {
    subPlanService.deleteSubPlan(totalPlanId, dailyPlanId, subPlanId, currentUser);
    return ResponseEntity.noContent().build();
  }
}
