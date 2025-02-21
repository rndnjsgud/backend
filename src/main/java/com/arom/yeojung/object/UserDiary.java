package com.arom.yeojung.object;

import com.arom.yeojung.object.dto.UserDiaryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDiary extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "diaryId", nullable = false)
    private Diary diary;

    private LocalDateTime createdDate;

    public static UserDiaryDto EntityToDto(UserDiary userDiary) {
        UserDiaryDto dto = new UserDiaryDto();
        dto.setCreatedDate(userDiary.getCreatedDate());
        dto.setUserId(userDiary.getUser().getId());
        dto.setDiaryId(userDiary.getDiary().getId());
        return dto;
    }
}
