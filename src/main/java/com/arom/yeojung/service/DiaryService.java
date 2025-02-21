package com.arom.yeojung.service;

import com.arom.yeojung.object.*;
import com.arom.yeojung.object.dto.DiaryDto;
import com.arom.yeojung.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final DiaryContentRepository diaryContentRepository;
    private final FileRepository fileRepository;
    private final UserDiaryRepository userDiaryRepository;
    private final FileS3UploadService fileS3UploadService;

    //다이어리 생성
    public DiaryDto createDiary(DiaryDto diaryDto) {
        //dto의 uerId를 통해 UserRepository에서 사용자 검색
        User user = userRepository.findById(diaryDto.getUserId())
                .orElseThrow(() -> new RuntimeException("not found user"));

        Diary diary = new Diary();
        diary.setUser(user);
        diary.setCreatedDate(LocalDateTime.now());
        diary.setUpdatedDate(LocalDateTime.now());
        diary.setTitle(diaryDto.getTitle());
        diary.setViewCount(0L);
        diary.setCommentCount(0L);  //댓글이라 좋아요 수 추가
        diary.setLikeCount(0L);     //
        diary.setStatus(diaryDto.getStatus());
        //썸네일은 기본적으로 첫번째 사진으로 지정 이후에 변경할 수 있도록 기능 구현
        diary.setThumbnailFile(fileRepository.findAll().getFirst());

        //UserDiary 자동 생성 (사용자와 다이어리 연결)
        UserDiary userDiary = new UserDiary();
        userDiary.setUser(user);
        userDiary.setDiary(diary);
        userDiary.setCreatedDate(LocalDateTime.now());

        userDiaryRepository.save(userDiary);

        //다이어리 저장
        diaryRepository.save(diary);
        //DiaryContent 저장
 /*       List<DiaryContent> contents = diaryDto.getContents()
                .stream().map(contentDto -> {
                    DiaryContent diaryContent = new DiaryContent();

                    diaryContent.setDiary(diary);
                    diaryContent.setContentType(contentDto.getContentType());

                    //컨텐츠 타입이 텍스트나 링크라면 그냥 컨텐츠를 저장하고 이미지나 비디오라면 파일을 찾아서 저장
                    if(contentDto.getContentType() == ContentType.TEXT || contentDto.getContentType() == ContentType.LINK) {
                        diaryContent.setContent(contentDto.getContent());
                    } else if(contentDto.getContentType() == ContentType.IMAGE || contentDto.getContentType() == ContentType.VIDEO){
                        //파일아이디로 파일을 찾아서 파일을 저장
                        diaryContent.setFile(fileRepository.findById(contentDto.getFileId())
                                .orElseThrow(() -> new RuntimeException("not found file")));
                    }
                    //Content의 순서 저장
                    diaryContent.setSequence(contentDto.getSequence());

                    return diaryContentRepository.save(diaryContent);
                }).collect(Collectors.toList());

        //diary에 컨텐츠들 저장
        diary.setContents(contents);*/
        return Diary.EntityToDto(diary);
    }

    //다이어리 조회
    public DiaryDto getDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("not found diary"));
        return Diary.EntityToDto(diary);
    }

    //전체 다이어리 조회
    public List<DiaryDto> getAllDiary() {
        return diaryRepository.findAll().stream().map(Diary::EntityToDto).collect(Collectors.toList());
    }

    //다이어리 수정
    @Transactional
    public DiaryDto updateDiary(Long diaryId, DiaryDto diaryDto) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("not found diary"));

        diary.setTitle(diaryDto.getTitle());
        diary.setUpdatedDate(LocalDateTime.now());
        diary.setStatus(diaryDto.getStatus());
        //썸네일 변경은 별도의 메소드로 구현

        return Diary.EntityToDto(diary);
    }

    //다이어리 썸네일 수정(새로운 사진으로 변경)
    @Transactional
    public File updateDiaryThumbnailNew(Long diaryId, MultipartFile file) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NoSuchElementException("not found diary"));

        File ThumbnailFile = fileS3UploadService.uploadAndSaveFile(file);
        diary.setThumbnailFile(ThumbnailFile);
        return ThumbnailFile;
    }

    //다이어리 썸네일 수정(원래 있던 사진으로 변경)
    @Transactional
    public File updateDiaryThumbnail(Long diaryId, Long fileId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NoSuchElementException("not found diary"));

        File ThumbnailFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new NoSuchElementException("not found file"));
        diary.setThumbnailFile(ThumbnailFile);
        return ThumbnailFile;
    }

    //다이어리 삭제
    public void deleteDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("not found diary"));
        //다이어리 컨텐츠들 모두 삭제
        diary.getContents().forEach(diaryContentRepository::delete);
        diaryRepository.delete(diary);
    }
}