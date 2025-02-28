package com.arom.yeojung.object;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "diaryId", nullable = false)
    private Diary diary;

    private String content;

    public static CommentDto EntityToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedDate());
        return commentDto;
    }
}
