package com.arom.yeojung.service;

import com.arom.yeojung.object.BaseTimeEntity;
import com.arom.yeojung.object.dto.DiaryContentDto;
import com.arom.yeojung.object.Diary;
import com.arom.yeojung.object.DiaryContent;
import com.arom.yeojung.object.File;
import com.arom.yeojung.repository.DiaryContentRepository;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryContentService extends BaseTimeEntity {

    private final DiaryContentRepository diaryContentRepository;
    private final DiaryRepository diaryRepository;
    private final FileS3UploadService fileS3UploadService;
    private final FileRepository fileRepository;


    //다이어리에 컨텐츠 추가(이미지나 비디오)
    @Transactional
    public DiaryContentDto addDiaryContentMedia(Long diaryId, MultipartFile file, DiaryContentDto contentDto) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NoSuchElementException("Diary not found"));

        //예외처리 수정
        //새로운 파일 업로드
        File upLoadFile = fileS3UploadService.uploadAndSaveFile(file);

        //업로드한 파일은 포함하는 Content 객체 생성
        DiaryContent diaryContent = new DiaryContent();
        diaryContent.setDiary(diary);
        diaryContent.setContentType(contentDto.getContentType());
        diaryContent.setFile(upLoadFile);
        diaryContent.setCreatedDate(getCreatedDate());
        diaryContent.setSequence(contentDto.getSequence());

        //다이어리에 컨텐츠 리스트에 추가
        diaryRepository.save(diary);
        diaryContentRepository.save(diaryContent);
        return contentDto;
    }

    //다이어리에 컨텐츠 추가(텍스트나 링크)
    @Transactional
    public DiaryContentDto addDiaryContentText(Long diaryId, DiaryContentDto contentDto) {
        //예외처리 해야함
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NoSuchElementException("Diary not found"));
        DiaryContent content = new DiaryContent();

        content.setDiary(diary);
        content.setContentType(contentDto.getContentType());
        content.setSequence(contentDto.getSequence());

        diary.getContents().add(content);

        diaryRepository.save(diary);
        diaryContentRepository.save(content);
        return contentDto;
    }

    //다이어리 컨텐츠 수정
    @Transactional
    public DiaryContentDto updateDiaryContent(Long diaryId, Long contentId,DiaryContentDto contentDto) {

        //텍스트 수정


        //이미지 비디오 변경


        //순서 변경


        return contentDto;
    }

    //다이어리 컨텐츠 조회
    //예외처리
    public DiaryContentDto getDiaryContent(Long diaryId, Long contentId) {
         DiaryContent diaryContent = diaryContentRepository.findById(contentId).get();
         //컨텐츠의 다이어리 아이디가 전달 받은 아이디랑 같을때만 실행
         if(diaryContent.getDiary().getId().equals(diaryId)) {
             return DiaryContent.EntityToDto(diaryContent);
         }
         return null;
    }

    //다이어리 컨텐츠 모두 조회
//    public List<DiaryContentDto> getAllDiaryContents(Long diaryId) {
//}

    //다이어리 컨텐츠 삭제
    //예외처리
    @Transactional
    public void deleteDiaryContent(Long diaryId, Long contentId) {
        DiaryContent diaryContent = diaryContentRepository.findById(contentId).get();
        if(diaryContent.getDiary().getId().equals(diaryId)) {
            diaryContentRepository.delete(diaryContent);
        }
    }
}
