package com.arom.yeojung.service;

import com.arom.yeojung.object.CheckList;
import com.arom.yeojung.object.TotalPlan;
import com.arom.yeojung.object.dto.CheckListRequestDTO;
import com.arom.yeojung.object.dto.CheckListResponseDTO;
import com.arom.yeojung.repository.CheckListRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final TotalPlanRepository totalPlanRepository;

    public CheckListResponseDTO createCheckList(CheckListRequestDTO requestDTO) {
        // DTO에 포함된 planId를 사용해 연결된 TotalPlan 엔티티 조회
        TotalPlan totalPlan = totalPlanRepository.findById(requestDTO.getTotalPlanId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHECKLIST_NOT_FOUND));

        // CheckList 엔티티 생성 및 DTO 값 반영
        CheckList checkList = new CheckList();
        checkList.setTask(requestDTO.getTask());
        checkList.setIsChecked(requestDTO.getIsChecked());
        checkList.setDescription(requestDTO.getDescription());
        checkList.setTotalPlan(totalPlan);

        // 엔티티 저장
        CheckList savedCheckList = checkListRepository.save(checkList);

        // 저장된 엔티티를 ResponseDTO로 매핑하여 반환
        return mapToResponseDTO(savedCheckList);
    }

    // 한 planId에 속하는 모든 체크리스트 조회
    public List<CheckListResponseDTO> getCheckListsByTotalPlanId(Long planId) {
        List<CheckList> checklistList = checkListRepository.findByTotalPlan_TotalPlanId(planId);
        return checklistList.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 전체 체크리스트 조회 메소드
    public List<CheckListResponseDTO> getAllCheckLists() {
        List<CheckList> checklistList = checkListRepository.findAll();
        return checklistList.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 체크리스트 수정 메소드
    public CheckListResponseDTO updateCheckList(Long id, CheckListRequestDTO updateDTO) {
        // 수정할 체크리스트 조회
        CheckList checklist = checkListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Checklist not found with id: " + id));

        // 만약 planId가 업데이트 요청에 포함되어 있고, 기존의 totalPlan과 다르다면 TotalPlan을 다시 조회 후 업데이트
        if (updateDTO.getTotalPlanId() != null &&
                !checklist.getTotalPlan().getTotalPlanId().equals(updateDTO.getTotalPlanId())) {
            TotalPlan totalPlan = totalPlanRepository.findById(updateDTO.getTotalPlanId())
                    .orElseThrow(() -> new CustomException(ErrorCode.TOTAL_PLAN_NOT_FOUND));
                            //new EntityNotFoundException("TotalPlan not found with id: " + updateDTO.getTotalPlanId()));
            checklist.setTotalPlan(totalPlan);
        }

        // 필드 업데이트
        checklist.setTask(updateDTO.getTask());
        checklist.setIsChecked(updateDTO.getIsChecked());
        checklist.setDescription(updateDTO.getDescription());

        // 업데이트된 엔티티 저장
        CheckList updatedChecklist = checkListRepository.save(checklist);
        return mapToResponseDTO(updatedChecklist);
    }

    // 체크리스트 삭제 메소드
    public void deleteCheckList(Long id) {
        // 삭제할 체크리스트 존재 여부 확인
        CheckList checkList = checkListRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CHECKLIST_NOT_FOUND));
        // 체크리스트 삭제
        checkListRepository.delete(checkList);
    }

    // Checklist 엔티티를 ChecklistResponseDTO로 변환하는 헬퍼 메소드
    public CheckListResponseDTO mapToResponseDTO(CheckList checklist) {
        CheckListResponseDTO responseDTO = new CheckListResponseDTO();
        responseDTO.setCheckListId(checklist.getCheckListId());
        responseDTO.setTask(checklist.getTask());
        responseDTO.setIsChecked(checklist.getIsChecked());
        responseDTO.setDescription(checklist.getDescription());
        responseDTO.setTotalPlanId(checklist.getTotalPlan().getTotalPlanId());
        return responseDTO;
    }
}
