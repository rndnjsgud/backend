package com.arom.yeojung.service;

import com.arom.yeojung.object.DailyPlan;
import com.arom.yeojung.object.TotalPlan;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.constants.PlanRole;
import com.arom.yeojung.object.dto.DailyPlanRequestDTO;
import com.arom.yeojung.object.dto.DailyPlanResponseDTO;
import com.arom.yeojung.repository.DailyPlanRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyPlanService {

    private final DailyPlanRepository dailyPlanRepository;
    private final TotalPlanRepository totalPlanRepository;

    // 생성
    @Transactional
    public List<DailyPlanResponseDTO> createDailyPlansForTotalPlan(
        Long totalPlanId, DailyPlanRequestDTO requestDTO, User currentUser) {

        TotalPlan totalPlan = totalPlanRepository.findById(totalPlanId)
            .orElseThrow(() -> new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND));
        validateOwnerAuthorization(totalPlan, currentUser);

        List<DailyPlan> existingPlans = dailyPlanRepository.findByTotalPlan(totalPlan);
        if (existingPlans != null && !existingPlans.isEmpty()) {
            throw new CustomException(ErrorCode.DAILY_PLAN_ALREADY_EXISTS);
        }

        List<DailyPlan> newDailyPlans = new ArrayList<>();

        // 0번째 DailyPlan 생성 (TotalPlan 전체를 대표)
        DailyPlan totalDailyPlan = new DailyPlan();
        totalDailyPlan.setTotalPlan(totalPlan);
        totalDailyPlan.setDailyPlanDate(totalPlan.getStartDate()); // 필요에 따라 별도 로직 적용 가능
        totalDailyPlan.setTripDayNumber(0);
        newDailyPlans.add(totalDailyPlan);

        // startDate부터 endDate까지 DailyPlan 생성
        LocalDate currentDate = totalPlan.getStartDate();
        LocalDate endDate = totalPlan.getEndDate();
        int dayNumber = 1;
        while (!currentDate.isAfter(endDate)) {
            DailyPlan dailyPlan = new DailyPlan();
            dailyPlan.setTotalPlan(totalPlan);
            dailyPlan.setDailyPlanDate(currentDate);
            dailyPlan.setTripDayNumber(dayNumber++);
            newDailyPlans.add(dailyPlan);
            currentDate = currentDate.plusDays(1);
        }

        dailyPlanRepository.saveAll(newDailyPlans);

        return newDailyPlans.stream()
            .map(plan -> DailyPlanResponseDTO.builder()
                .dailyPlanDate(plan.getDailyPlanDate())
                .tripDayNumber(plan.getTripDayNumber())
                .build())
            .collect(Collectors.toList());
    }


    // 단일 DailyPlan 조회
    @Transactional(readOnly = true)
    public DailyPlanResponseDTO getDailyPlanById(Long totalPlanId, Long dailyPlanId) {
        DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);
        return DailyPlanResponseDTO.builder()
            .dailyPlanDate(dailyPlan.getDailyPlanDate())
            .tripDayNumber(dailyPlan.getTripDayNumber())
            .build();
    }

    // 수정
    @Transactional
    public DailyPlanResponseDTO updateDailyPlan(Long totalPlanId, Long dailyPlanId,
        DailyPlanRequestDTO requestDTO, User currentUser) {
        DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);
        validateOwnerAuthorization(dailyPlan.getTotalPlan(), currentUser);

        dailyPlan.setDailyPlanDate(requestDTO.getDailyPlanDate());
        dailyPlan.setTripDayNumber(requestDTO.getTripDayNumber());
        dailyPlan.markAsUpdated();

        dailyPlanRepository.save(dailyPlan);

        return DailyPlanResponseDTO.builder()
            .dailyPlanDate(dailyPlan.getDailyPlanDate())
            .tripDayNumber(dailyPlan.getTripDayNumber())
            .build();
    }

    // 삭제
    @Transactional
    public void deleteDailyPlan(Long totalPlanId, Long dailyPlanId, User currentUser) {
        DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);
        validateOwnerAuthorization(dailyPlan.getTotalPlan(), currentUser);

        dailyPlan.markAsDeleted();
        dailyPlanRepository.save(dailyPlan);
    }

    // DailyPlan 존재 여부 및 TotalPlan 매칭 확인
    private DailyPlan findDailyPlanWithValidation(Long totalPlanId, Long dailyPlanId) {
        DailyPlan dailyPlan = dailyPlanRepository.findById(dailyPlanId)
            .orElseThrow(() -> new CustomException(ErrorCode.DAILY_PLAN_NOT_FOUND));
        if (!dailyPlan.getTotalPlan().getTotalPlanId().equals(totalPlanId)) {
            throw new CustomException(ErrorCode.DAILY_PLAN_NOT_IN_TOTAL_PLAN);
        }
        return dailyPlan;
    }

    private void validateOwnerAuthorization(TotalPlan totalPlan, User currentUser) {
        boolean isOwner = totalPlan.getMembers().stream()
            .anyMatch(up -> up.getUser().equals(currentUser)
                && up.getRole() == PlanRole.OWNER);
        if (!isOwner) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }


}
