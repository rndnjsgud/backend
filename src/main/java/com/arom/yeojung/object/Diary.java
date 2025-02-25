package com.arom.yeojung.object;

import com.arom.yeojung.object.dto.DiaryContentDto;
import com.arom.yeojung.object.dto.DiaryDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diary extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    //@OneToOne
    //private TotalPlan totalPlan;
    //계획을 전체 계획이랑 연결할건지 세부계획이랑 연결할건지 회의

    //제목
    private String title;

    //조회수
    private Long viewCount;
    //댓글수
    private Long commentCount;
    //좋아요 수
    private Long likeCount;

    //썸네일 파일
    @OneToOne
    private File thumbnailFile;

    //현재 다이어리의 상태
    //DRAFT(임시저쟝) PUBLIC(공개) PRIVATE(비공개)
    @Enumerated(EnumType.STRING)
    private DiaryStatus status;

    //컨테츠 들을 담은 리스트 순서 오름차순으로 정렬
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sequence ASC")
    private List<DiaryContent> contents = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<UserDiary> userDiaries = new ArrayList<>();

    //Diary -> DiaryDto 변환 메소드
    public static DiaryDto EntityToDto(Diary diary) {
        DiaryDto dto = new DiaryDto();
        dto.setTitle(diary.title);
        dto.setStatus(diary.status);
        dto.setCreatedDate(diary.getCreatedDate());
        dto.setUpdatedDate(diary.getUpdatedDate());
        dto.setViewCount(diary.viewCount);
        dto.setCommentCount(diary.commentCount);
        dto.setLikeCount(diary.likeCount);
        dto.setUserId(diary.user.getId());

        //컨텐츠 리스트를 컨텐츠 디티오로 변환하여 저장
        /*List<DiaryContentDto> contentDtos = diary.getContents()
                        .stream().map(DiaryContent::EntityToDto)
                        .collect(Collectors.toList());*/
        //dto.setContents(contentDtos);
        return dto;
    }
}