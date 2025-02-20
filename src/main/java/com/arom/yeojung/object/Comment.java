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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private User user;

    @ManyToOne
    @JoinColumn(name = "diaryid", nullable = false)
    private Diary diary;

    private String content;
    private LocalDateTime createdAt;
}
