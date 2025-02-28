package com.arom.yeojung.repository;

import com.arom.yeojung.object.UserDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {
    Optional<List<UserDiary>> findAllByUserId(Long userId);
    Optional<List<UserDiary>> findAllByDiaryId(Long diaryId);
    void deleteByUserIdAndDiaryId(Long userId, Long diaryId);
    Optional<UserDiary> findByUserIdAndDiaryId(Long userId, Long diaryId);
}
