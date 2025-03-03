package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.user.FriendDto;
import com.arom.yeojung.object.dto.user.UserDto;
import com.arom.yeojung.service.FriendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(
    name = "친구 관리 API",
    description = "친구 관리 API 제공"
)
public class FriendController {

  private final FriendService friendService;

  // 친구 요청 보내기
  @PostMapping("/request")
  public ResponseEntity<Void> sendFriendRequest(
      @RequestParam Long senderId,
      @RequestParam Long receiverId) {
    friendService.sendFriendRequest(senderId, receiverId);
    return ResponseEntity.ok().build();
  }

  // 친구 요청 수락
  @PostMapping("/accept/{requestId}")
  public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
    friendService.acceptFriendRequest(requestId);
    return ResponseEntity.ok().build();
  }

  // 친구 요청 거절
  @PostMapping("/reject/{requestId}")
  public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long requestId) {
    friendService.rejectFriendRequest(requestId);
    return ResponseEntity.ok().build();
  }

  // 친구 요청 받은 사용자 리스트 조회
  @GetMapping("/requests")
  public ResponseEntity<List<UserDto>> getFriendRequests(@RequestParam Long userId) {
    List<UserDto> friendRequests = friendService.getFriendRequest(userId);
    return ResponseEntity.ok(friendRequests);
  }

  // 사용자의 친구 리스트 조회
  @GetMapping("/list")
  public ResponseEntity<List<FriendDto>> getFriendList(@RequestParam Long userId) {
    List<FriendDto> friendList = friendService.getFriendList(userId);
    return ResponseEntity.ok(friendList);
  }
}