package com.arom.yeojung.repository;

import com.arom.yeojung.object.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
