package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.user.FollowRequest;
import com.arom.yeojung.object.dto.user.FollowResponse;
import com.arom.yeojung.service.FollowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
    name = "팔로우 관리 API",
    description = "회원 팔로우 관리 API 제공"
)
public class FollowController {

  private final FollowService followService;

  // 사용자 팔로우
  @PostMapping("/follow")
  public ResponseEntity<Void> follow(
      @RequestBody FollowRequest request) {
    followService.followUser(request);
    return ResponseEntity.ok().build();
  }

  // 팔로우 취소
  @DeleteMapping("/unfollow")
  public ResponseEntity<Void> unfollow(
      @RequestBody FollowRequest request) {
    followService.unfollowUser(request);
    return ResponseEntity.ok().build();
  }

  // 특정 사용자의 팔로워 목록 조회
  @GetMapping("/{userId}/followers")
  public ResponseEntity<Page<FollowResponse>> getFollowers(@PathVariable Long userId) {
    return ResponseEntity.ok(followService.getFollowers(userId));
  }

  // 특정 사용자의 팔로잉 목록 조회
  @GetMapping("/{userId}/followings")
  public ResponseEntity<Page<FollowResponse>> getFollowings(
      @PathVariable Long userId) {
    return ResponseEntity.ok(followService.getFollowings(userId));
  }
}
