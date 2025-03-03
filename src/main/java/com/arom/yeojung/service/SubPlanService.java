package com.arom.yeojung.service;

import com.arom.yeojung.object.DailyPlan;
import com.arom.yeojung.object.SubPlan;
import com.arom.yeojung.object.TotalPlan;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.SubPlanRequestDTO;
import com.arom.yeojung.object.dto.SubPlanResponseDTO;
import com.arom.yeojung.repository.DailyPlanRepository;
import com.arom.yeojung.repository.SubPlanRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubPlanService {

  private final SubPlanRepository subPlanRepository;
  private final DailyPlanRepository dailyPlanRepository;
  private final TotalPlanRepository totalPlanRepository;

  // 단일 SubPlan 조회
  @Transactional(readOnly = true)
  public SubPlanResponseDTO getSubPlanById(Long totalPlanId, Long dailyPlanId, Long subPlanId) {
    SubPlan subPlan = findSubPlanWithValidation(totalPlanId, dailyPlanId, subPlanId);
    // 부모 TotalPlan이나 DailyPlan이 soft delete 상태라면 에러
    if (subPlan.getDailyPlan().getTotalPlan().getIsDeleted() ||
        subPlan.getDailyPlan().getIsDeleted() ||
        subPlan.getIsDeleted()) {
      throw new CustomException(ErrorCode.SUB_PLAN_ALREADY_DELETED);
    }
    return SubPlanResponseDTO.builder()
        .subPlanId(subPlan.getSubPlanId())
        .subPlanTitle(subPlan.getSubPlanTitle())
        .subPlanDescription(subPlan.getSubPlanDescription())
        .subPlanTime(subPlan.getSubPlanTime())
        .build();
  }

  // 특정 DailyPlan에 속한 모든 SubPlan 조회
  @Transactional(readOnly = true)
  public List<SubPlanResponseDTO> getAllSubPlans(Long totalPlanId, Long dailyPlanId) {
    DailyPlan dailyPlan = findDailyPlanWithValidation(totalPlanId, dailyPlanId);
    List<SubPlan> subPlans = subPlanRepository.findByDailyPlan(dailyPlan);
    return subPlans.stream()
        .filter(sp -> !sp.getIsDeleted())
        .map(sp -> SubPlanResponseDTO.builder()
            .subPlanId(sp.getSubPlanId())
            .subPlanTitle(sp.getSubPlanTitle())
            .subPlanDescription(sp.getSubPlanDescription())
            .subPlanTime(sp.getSubPlanTime())
            .build())
        .collect(Collectors.toList());
  }

  // 생성
  @Transactional
  public SubPlanResponseDTO createSubPlan(Long totalPlanId, Long dailyPlanId,
      SubPlanRequestDTO requestDTO, User currentUser) {
    // TotalPlan 조회 및 권한 검증
    TotalPlan totalPlan = totalPlanRepository.findById(totalPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND));
    validateAuthorization(totalPlan, currentUser);
    // DailyPlan 조회 및 검증
    DailyPlan dailyPlan = dailyPlanRepository.findById(dailyPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.DAILY_PLAN_NOT_FOUND));
    if (!dailyPlan.getTotalPlan().getTotalPlanId().equals(totalPlanId)) {
      throw new CustomException(ErrorCode.DAILY_PLAN_NOT_IN_TOTAL_PLAN);
    }
    if (dailyPlan.getIsDeleted()) {
      throw new CustomException(ErrorCode.DAILY_PLAN_ALREADY_DELETED);
    }

    SubPlan subPlan = new SubPlan();
    subPlan.setDailyPlan(dailyPlan);
    subPlan.setSubPlanTitle(requestDTO.getSubPlanTitle());
    subPlan.setSubPlanDescription(requestDTO.getSubPlanDescription());
    subPlan.setSubPlanTime(requestDTO.getSubPlanTime());
    subPlan.setCreatedBy(currentUser);


    subPlanRepository.save(subPlan);

    return SubPlanResponseDTO.builder()
        .subPlanId(subPlan.getSubPlanId())
        .subPlanTitle(subPlan.getSubPlanTitle())
        .subPlanDescription(subPlan.getSubPlanDescription())
        .subPlanTime(subPlan.getSubPlanTime())
        .build();
  }

  // 수정
  @Transactional
  public SubPlanResponseDTO updateSubPlan(Long totalPlanId, Long dailyPlanId, Long subPlanId,
      SubPlanRequestDTO requestDTO, User currentUser) {
    SubPlan subPlan = findSubPlanWithValidation(totalPlanId, dailyPlanId, subPlanId);
    // 권한 검증
    validateAuthorization(subPlan.getDailyPlan().getTotalPlan(), currentUser);

    subPlan.setSubPlanTitle(requestDTO.getSubPlanTitle());
    subPlan.setSubPlanDescription(requestDTO.getSubPlanDescription());
    subPlan.setSubPlanTime(requestDTO.getSubPlanTime());
    subPlan.markAsUpdated();

    subPlanRepository.save(subPlan);
    return SubPlanResponseDTO.builder()
        .subPlanId(subPlan.getSubPlanId())
        .subPlanTitle(subPlan.getSubPlanTitle())
        .subPlanDescription(subPlan.getSubPlanDescription())
        .subPlanTime(subPlan.getSubPlanTime())
        .build();
  }

  // 삭제
  @Transactional
  public void deleteSubPlan(Long totalPlanId, Long dailyPlanId, Long subPlanId, User currentUser) {
    SubPlan subPlan = findSubPlanWithValidation(totalPlanId, dailyPlanId, subPlanId);
    validateAuthorization(subPlan.getDailyPlan().getTotalPlan(), currentUser);
    subPlan.markAsDeleted();
    subPlanRepository.save(subPlan);
  }

  // SubPlan이 해당 DailyPlan에 속하는지 확인
  private SubPlan findSubPlanWithValidation(Long totalPlanId, Long dailyPlanId, Long subPlanId) {
    SubPlan subPlan = subPlanRepository.findById(subPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.SUB_PLAN_NOT_FOUND));
    DailyPlan dailyPlan = subPlan.getDailyPlan();
    if (!dailyPlan.getTotalPlan().getTotalPlanId().equals(totalPlanId)) {
      throw new CustomException(ErrorCode.DAILY_PLAN_NOT_IN_TOTAL_PLAN);
    }
    if (!dailyPlan.getDailyPlanId().equals(dailyPlanId)) {
      throw new CustomException(ErrorCode.SUB_PLAN_NOT_IN_DAILY_PLAN);
    }
    return subPlan;
  }

  // DailyPlan 조회 검증 로직
  private DailyPlan findDailyPlanWithValidation(Long totalPlanId, Long dailyPlanId) {
    DailyPlan dailyPlan = dailyPlanRepository.findById(dailyPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.DAILY_PLAN_NOT_FOUND));
    if (!dailyPlan.getTotalPlan().getTotalPlanId().equals(totalPlanId)) {
      throw new CustomException(ErrorCode.DAILY_PLAN_NOT_IN_TOTAL_PLAN);
    }
    return dailyPlan;
  }

  // TotalPlan에 연결된 멤버 확인
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
