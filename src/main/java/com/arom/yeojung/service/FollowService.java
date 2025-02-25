package com.arom.yeojung.service;

import com.arom.yeojung.object.Follow;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.user.FollowRequest;
import com.arom.yeojung.object.dto.user.FollowResponse;
import com.arom.yeojung.repository.FollowRepository;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

  private final FollowRepository followRepository;
  private final UserRepository userRepository;

  // 사용자 팔로우
  @Transactional(readOnly = true)
  public void followUser(FollowRequest request) {
    User follower = userRepository.findByUserId(request.getFollowerId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    User followee = userRepository.findByUserId(request.getFolloweeId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Follow follow = Follow.builder()
        .follower(follower)
        .followee(followee)
        .build();

    log.info("{} 사용자가 {} 사용자를 팔로우했습니다.", follower.getUserId(), followee.getUserId());
    followRepository.save(follow);
  }

  // 사용자 팔로우 취소
  @Transactional
  public void unfollowUser(FollowRequest request) {
    User follower = userRepository.findByUserId(request.getFollowerId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    User followee = userRepository.findByUserId(request.getFolloweeId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
        .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_RELATION_NOT_FOUND));

    followRepository.delete(follow);
    log.info("{} 사용자가 {} 사용자를 언팔로우했습니다.", follower.getUserId(), followee.getUserId());
    ResponseEntity.ok().build();
  }

  // 사용자의 팔로워 조회 (사용자를 팔로우하는 사람)
  @Transactional(readOnly = true)
  public Page<FollowResponse> getFollowers(Long userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    
    Pageable pageable =
        PageRequest.of(0, 20, Sort.by("createdDate").descending());
    Page<Follow> followers = followRepository.findAllByFollowee(user, pageable);

    return followers.map(follow -> FollowResponse.builder()
        .follower(follow.getFollower())
        .followee(follow.getFollowee())
        .build()
    );
  }

  // 사용자의 팔로잉 조회 (사용자가 팔로우하는 사람)
  @Transactional(readOnly = true)
  public Page<FollowResponse> getFollowings(Long userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Pageable pageable =
        PageRequest.of(0, 20, Sort.by("createdDate").descending());
    Page<Follow> followings = followRepository.findAllByFollower(user, pageable);

    return followings.map(follow -> FollowResponse.builder()
        .follower(follow.getFollower())
        .followee(follow.getFollowee())
        .build()
    );
  }
}