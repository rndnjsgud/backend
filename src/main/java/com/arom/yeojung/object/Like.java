package com.arom.yeojung.object;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private User user;

    @ManyToOne
    @JoinColumn(name = "diaryid", nullable = false)
    private Diary diary;

    private LocalDateTime createdAt;
}
