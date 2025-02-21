package com.arom.yeojung.object.dto;

import com.arom.yeojung.object.ContentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryContentDto {
    //다이어리 아이디
    private Long diaryId;
    private ContentType contentType;
    private String content;
    //파일 아이디
    private Long fileId;
    //콘텐츠의 순서
    private Long sequence;
}