package com.arom.yeojung.controller;

import com.arom.yeojung.object.File;
import com.arom.yeojung.object.dto.DiaryContentDto;
import com.arom.yeojung.object.dto.DiaryDto;
import com.arom.yeojung.service.DiaryContentService;
import com.arom.yeojung.service.DiaryService;
import com.arom.yeojung.service.FileS3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final DiaryContentService diaryContentService;
    private final FileS3UploadService fileS3UploadService;

    //다이어리 생성 Create
    @PostMapping
    public ResponseEntity<DiaryDto> createDiary(@RequestBody DiaryDto diaryDto) {
        return ResponseEntity.ok(diaryService.createDiary(diaryDto));
    }

    //특정 다이어리 조회 Read
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDto> getDiary(@PathVariable Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(diaryId));
    }

    //모든 다이어리 조회
    @GetMapping
    public ResponseEntity<List<DiaryDto>> getAllDiaries() {
        return ResponseEntity.ok(diaryService.getAllDiary());
    }

    //다이어리 수정 Update
    @PutMapping("/{diaryId}")
    public ResponseEntity<DiaryDto> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryDto diaryDto) {
        return ResponseEntity.ok((diaryService.updateDiary(diaryId, diaryDto)));
    }

    //다이어리 수정 종류
    //1. 컨텐츠 순서 변경
    //2. 컨텐츠 이미지나 비디오 변경
    //3. 텍스트 수정
    //4. 공개 비공개 변경

    //다이어리 썸네일 변경(새로운 사진으로 변경)
    @PutMapping("{diaryId}/thumbnail")
    public ResponseEntity<File> updateDiaryThumbnail(@PathVariable Long diaryId, @ModelAttribute MultipartFile file) {
        return ResponseEntity.ok(diaryService.updateDiaryThumbnailNew(diaryId, file));
    }

    //다이어리 썸네일 변경(원래 있던 사진으로 변경)
    @PutMapping("{diaryId}/{fileId}")
    public ResponseEntity<File> updateDiaryThumbnail(@PathVariable Long diaryId, @PathVariable Long fileId) {
        return ResponseEntity.ok(diaryService.updateDiaryThumbnail(diaryId, fileId));
    }

    //다이어리 삭제 Delete
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        //해당 다이어리에 컨텐츠들 모두 삭제
        return ResponseEntity.ok("Diary deleted.");
    }

    //다이어리 사용자 추가

    //특정 다이어리에 diaryContent 추가(이미지나 비디오)
    @PostMapping("/{diaryId}/contents/text")
    public ResponseEntity<DiaryContentDto> addDiaryContentMedia(
            @PathVariable Long diaryId,
            @ModelAttribute MultipartFile file,
            @RequestBody DiaryContentDto diaryContentDto
    ) {
        return ResponseEntity.ok(diaryContentService.addDiaryContentMedia(diaryId, file, diaryContentDto));
    }

    //특정 다이어리에 diaryContent 추가(텍스트나 링크)
    @PostMapping("/{diaryId}/contents/media")
    public ResponseEntity<DiaryContentDto> addDiaryContentText(
            @PathVariable Long diaryId,
            @RequestBody DiaryContentDto diaryContentDto
    ) {
        return ResponseEntity.ok(diaryContentService.addDiaryContentText(diaryId, diaryContentDto));
    }


    //다이어리의 특정 diaryContent 수정
    @PutMapping("/{diaryId}/contents/{contentId}")
    public ResponseEntity<DiaryContentDto> updateDiaryContent(
            @PathVariable Long diaryId,
            @PathVariable Long contentId,
            @RequestBody DiaryContentDto contentDto
    ) {
        return ResponseEntity.ok(diaryContentService.updateDiaryContent(diaryId, contentId, contentDto));
    }


    //다이어리에서 특정 diaryContent 삭제
    @DeleteMapping("/{diaryId}/contents/{contentId}")
    public ResponseEntity<String> deleteDiaryContent(
            @PathVariable Long diaryId,
            @PathVariable Long contentId
    ) {
        diaryContentService.deleteDiaryContent(diaryId, contentId);
        return ResponseEntity.ok("Content deleted successfully.");
    }
}