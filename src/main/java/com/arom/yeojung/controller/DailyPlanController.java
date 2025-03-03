package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.DailyPlanRequestDTO;
import com.arom.yeojung.object.dto.DailyPlanResponseDTO;
import com.arom.yeojung.object.DailyPlan;
import com.arom.yeojung.object.TotalPlan;
import com.arom.yeojung.object.User;
import com.arom.yeojung.repository.DailyPlanRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.service.DailyPlanService;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/total-plans/{totalPlanId}/daily-plans")
@RequiredArgsConstructor
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    // totalPlan 내에서 특정 dailyPlan 조회
    @GetMapping("/{dailyPlanId}")
    public ResponseEntity<DailyPlanResponseDTO> getDailyPlan(
            @PathVariable Long totalPlanId,
            @PathVariable Long dailyPlanId) {
        DailyPlanResponseDTO response = dailyPlanService.getDailyPlanById(totalPlanId, dailyPlanId);
        return ResponseEntity.ok(response);
    }

    // 생성 (totalPlan에 속한 dailyPlan 추가)
    @PostMapping
    public ResponseEntity<List<DailyPlanResponseDTO>> createDailyPlans(
        @PathVariable Long totalPlanId,
        @RequestBody @Valid DailyPlanRequestDTO requestDTO,
        @AuthenticationPrincipal User currentUser) {
        List<DailyPlanResponseDTO> responses = dailyPlanService.createDailyPlansForTotalPlan(totalPlanId, requestDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }


    // 수정
    @PutMapping("/{dailyPlanId}")
    public ResponseEntity<DailyPlanResponseDTO> updateDailyPlan(
            @PathVariable Long totalPlanId,
            @PathVariable Long dailyPlanId,
            @RequestBody @Valid DailyPlanRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) {
        DailyPlanResponseDTO response = dailyPlanService.updateDailyPlan(totalPlanId, dailyPlanId, requestDTO, currentUser);
        return ResponseEntity.ok(response);
    }

    // 삭제
    @DeleteMapping("/{dailyPlanId}")
    public ResponseEntity<Void> deleteDailyPlan(
            @PathVariable Long totalPlanId,
            @PathVariable Long dailyPlanId,
            @AuthenticationPrincipal User currentUser) {
        dailyPlanService.deleteDailyPlan(totalPlanId, dailyPlanId, currentUser);
        return ResponseEntity.noContent().build();
    }
}