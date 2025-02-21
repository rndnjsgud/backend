package com.arom.yeojung.service;

import com.arom.yeojung.object.BaseTimeEntity;
import com.arom.yeojung.object.Diary;
import com.arom.yeojung.object.Like;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.LikeDto;
import com.arom.yeojung.repository.DiaryRepository;
import com.arom.yeojung.repository.LikeRepository;
import com.arom.yeojung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LikeService extends BaseTimeEntity {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    //좋아요 생성
    public LikeDto createLike(LikeDto likeDto) {
        User user = userRepository.findById(likeDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("not found user"));

        Diary diary = diaryRepository.findById(likeDto.getDiaryId())
                .orElseThrow(() -> new NoSuchElementException("not found diary"));

        Like like = new Like();
        like.setUser(user);
        like.setDiary(diary);
        like.setCreatedDate(getCreatedDate());
        likeRepository.save(like);
        return likeDto;
    }

    //좋아요 삭제
    public String deleteLike(LikeDto likeDto) {
        Like like = likeRepository.findByUserIdAndDiaryId(likeDto.getUserId(), likeDto.getDiaryId())
                .orElseThrow(() -> new NoSuchElementException("not found like"));
        likeRepository.delete(like);
        return "success";
    }
}