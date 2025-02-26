package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.CheckListRequestDTO;
import com.arom.yeojung.object.dto.CheckListResponseDTO;
import com.arom.yeojung.service.CheckListService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checklists")
public class CheckListController {
    private final CheckListService  checkListService;

    //체크리스트 생성
    @PostMapping
    public ResponseEntity<CheckListResponseDTO> createCheckList(@RequestBody CheckListRequestDTO requestDTO) {
        CheckListResponseDTO responseDTO = checkListService.createCheckList(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    //특정 planId에 속한 체크리스트 조회
    @GetMapping("/{planId}")
    public ResponseEntity<List<CheckListResponseDTO>> getCheckListByPlanId(@PathVariable Long planId){
        List<CheckListResponseDTO> responseLists = checkListService.getCheckListsByTotalPlanId(planId);
        return ResponseEntity.ok(responseLists);
    }

    //모든 체크리스트 조회
    @GetMapping
    public ResponseEntity<List<CheckListResponseDTO>> getAllCheckLists(){
        List<CheckListResponseDTO> responseList = checkListService.getAllCheckLists();
        return ResponseEntity.ok(responseList);
    }

    //체크리스트 수정
    @PutMapping("/{id}")
    public ResponseEntity<CheckListResponseDTO> updateCheckList(@PathVariable Long id, @RequestBody CheckListRequestDTO updateDTO) {
        CheckListResponseDTO updatedCheckList = checkListService.updateCheckList(id, updateDTO);
        return ResponseEntity.ok(updatedCheckList);
    }

    //체크리스트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckList(@PathVariable Long id){
        checkListService.deleteCheckList(id);
        return ResponseEntity.noContent().build();
    }
}
