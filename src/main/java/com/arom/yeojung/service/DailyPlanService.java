package com.arom.yeojung.service;

import com.arom.yeojung.object.DailyPlan;
import com.arom.yeojung.object.TotalPlan;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.DailyPlanRequestDTO;
import com.arom.yeojung.object.dto.DailyPlanResponseDTO;
import com.arom.yeojung.repository.DailyPlanRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyPlanService {

    private final DailyPlanRepository dailyPlanRepository;
    private final TotalPlanRepository totalPlanRepository;

    // DailyPlan 조회 (dailyPlanId로 조회)
    @Transactional(readOnly = true)
    public DailyPlanResponseDTO getDailyPlanById(Long totalPlanId, Long dailyPlanId) {
        DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);

        return DailyPlanResponseDTO.builder()
                .dailyPlanDate(dailyPlan.getDailyPlanDate())
                .tripDayNumber(dailyPlan.getTripDayNumber())
                .build();
    }

    // DailyPlan 수정
    @Transactional
    public DailyPlanResponseDTO updateDailyPlan(Long totalPlanId, Long dailyPlanId, DailyPlanRequestDTO requestDTO, User currentUser) {
        DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);

        // 권한 확인
        validateAuthorization(dailyPlan.getTotalPlan(), currentUser);

        // 값 업데이트
        dailyPlan.setDailyPlanDate(requestDTO.getDailyPlanDate());
        dailyPlan.setTripDayNumber(requestDTO.getTripDayNumber());
        dailyPlanRepository.save(dailyPlan);

        return DailyPlanResponseDTO.builder()
                .dailyPlanDate(dailyPlan.getDailyPlanDate())
                .tripDayNumber(dailyPlan.getTripDayNumber())
                .build();
    }

    // DailyPlan 삭제
    @Transactional
    public void deleteDailyPlan(Long totalPlanId, Long dailyPlanId, User currentUser) {
        DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);

        // 권한 확인
        validateAuthorization(dailyPlan.getTotalPlan(), currentUser);

        dailyPlanRepository.delete(dailyPlan);
        // 삭제 표기로 바꾸기
    }

    // 중복 검증 로직 분리 (DailyPlan 존재 여부 및 TotalPlan 매칭 확인)
    private DailyPlan findDailyPlanWithValidation(Long totalPlanId, Long dailyPlanId) {
        DailyPlan dailyPlan = dailyPlanRepository.findById(dailyPlanId)
                .orElseThrow(() -> new CustomException(ErrorCode.DAILY_PLAN_NOT_FOUND));

        if (!dailyPlan.getTotalPlan().getTotalPlanId().equals(totalPlanId)) {
            throw new CustomException(ErrorCode.DAILY_PLAN_NOT_IN_TOTAL_PLAN);
        }
        return dailyPlan;
    }

    // 권한 검증 로직 분리
    private void validateAuthorization(TotalPlan totalPlan, User currentUser) {
        if (totalPlan == null) {
            throw new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND);
        }
        boolean isAuthorized = totalPlan.getMembers().stream()
                .anyMatch(up -> up.getUser().equals(currentUser));

        if (!isAuthorized) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}
