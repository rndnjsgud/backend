package com.arom.yeojung.repository;

import com.arom.yeojung.object.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndDiaryId(Long userId, Long diaryId);
}
