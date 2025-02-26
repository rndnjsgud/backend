package com.arom.yeojung.service;

import com.arom.yeojung.object.TotalPlan;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.UserPlan;
import com.arom.yeojung.object.constants.PlanRole;
import com.arom.yeojung.object.dto.TotalPlanRequestDTO;
import com.arom.yeojung.object.dto.TotalPlanResponseDTO;
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
public class TotalPlanService {

  private final TotalPlanRepository totalPlanRepository;

  // 단일 totalPlan 조회
  @Transactional(readOnly = true)
  public TotalPlanResponseDTO getTotalPlanById(Long totalPlanId) {
    TotalPlan totalPlan = totalPlanRepository.findById(totalPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND));

    if (totalPlan.getIsDeleted()) {
      throw new CustomException(ErrorCode.TOTAL_PLAN_ALREADY_DELETED);
    }

    return mapToResponseDto(totalPlan);
  }

  // 생성
  @Transactional
  public TotalPlanResponseDTO createTotalPlan(TotalPlanRequestDTO requestDTO, User currentUser) {

    TotalPlan totalPlan = new TotalPlan();
    totalPlan.setTitle(requestDTO.getTitle());
    totalPlan.setStartDate(requestDTO.getStartDate());
    totalPlan.setEndDate(requestDTO.getEndDate());
    totalPlan.setTotalBudget(requestDTO.getTotalBudget());
    totalPlan.setTotalPlanDescription(requestDTO.getTotalPlanDescription());
    totalPlan.setTravelDuration(requestDTO.getTravelDuration());

    // UserPlan 생성 (OWNER)
    UserPlan ownerUserPlan = new UserPlan();
    ownerUserPlan.setUser(currentUser);
    ownerUserPlan.setTotalPlan(totalPlan);
    ownerUserPlan.setRole(PlanRole.OWNER);

    totalPlan.getMembers().add(ownerUserPlan);

    totalPlanRepository.save(totalPlan);

    return mapToResponseDto(totalPlan);
  }

  // 수정
  @Transactional
  public TotalPlanResponseDTO updateTotalPlan(Long totalPlanId, TotalPlanRequestDTO requestDTO, User currentUser) {
    TotalPlan totalPlan = totalPlanRepository.findById(totalPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND));

    // 소프트 딜리트된 경우 수정 불가 처리
    if (totalPlan.getIsDeleted()) {
      throw new CustomException(ErrorCode.TOTAL_PLAN_ALREADY_DELETED);
    }

    validateOwnerAuthorization(totalPlan, currentUser);

    totalPlan.setTitle(requestDTO.getTitle());
    totalPlan.setStartDate(requestDTO.getStartDate());
    totalPlan.setEndDate(requestDTO.getEndDate());
    totalPlan.setTotalBudget(requestDTO.getTotalBudget());
    totalPlan.setTotalPlanDescription(requestDTO.getTotalPlanDescription());
    totalPlan.setTravelDuration(requestDTO.getTravelDuration());

    totalPlan.markAsUpdated();

    totalPlanRepository.save(totalPlan);

    return mapToResponseDto(totalPlan);
  }


  // 삭제
  @Transactional
  public void deleteTotalPlan(Long totalPlanId, User currentUser) {
    TotalPlan totalPlan = totalPlanRepository.findById(totalPlanId)
        .orElseThrow(() -> new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND));

    validateOwnerAuthorization(totalPlan, currentUser);

    // 이미 소프트 딜리트된 경우 에러 처리
    if (totalPlan.getIsDeleted()) {
      throw new CustomException(ErrorCode.TOTAL_PLAN_ALREADY_DELETED);
    }

    // 소프트 딜리트 처리
    totalPlan.markAsDeleted();
    totalPlanRepository.save(totalPlan);
  }

  // 모두 조회 (소프트 딜리트x만 반환)
  @Transactional(readOnly = true)
  public List<TotalPlanResponseDTO> getAllTotalPlans() {
    List<TotalPlan> totalPlans = totalPlanRepository.findAll();
    return totalPlans.stream()
        .filter(plan -> !plan.getIsDeleted())
        .map(this::mapToResponseDto)
        .collect(Collectors.toList());
  }

  // OWNER 권한 검증
  private void validateOwnerAuthorization(TotalPlan totalPlan, User currentUser) {
    boolean isOwner = totalPlan.getMembers().stream()
        .anyMatch(up -> up.getUser().equals(currentUser)
                && up.getRole() == com.arom.yeojung.object.constants.PlanRole.OWNER);

    if (!isOwner) {
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }
  }

  private TotalPlanResponseDTO mapToResponseDto(TotalPlan totalPlan) {
    return TotalPlanResponseDTO.builder()
        .totalPlanId(totalPlan.getTotalPlanId())
        .title(totalPlan.getTitle())
        .startDate(totalPlan.getStartDate())
        .endDate(totalPlan.getEndDate())
        .totalBudget(totalPlan.getTotalBudget())
        .totalPlanDescription(totalPlan.getTotalPlanDescription())
        .travelDuration(totalPlan.getTravelDuration())
        .build();
  }
}
