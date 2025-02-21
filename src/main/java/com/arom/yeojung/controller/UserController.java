package com.arom.yeojung.controller;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.user.CustomUserDetails;
import com.arom.yeojung.object.dto.user.NicknameRequest;
import com.arom.yeojung.object.dto.user.ProfileImageRequest;
import com.arom.yeojung.object.dto.user.UserDto;
import com.arom.yeojung.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
    name = "회원 관리 API",
    description = "회원 관리 API 제공"
)
public class UserController {

  private final UserService userService;

  // userId로 회원 정보 조회
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getMemberInfo(
      @PathVariable("userId") Long userId) {
    UserDto userDto = userService.getMemberInfo(userId);
    return ResponseEntity.ok(userDto);
  }

  // 닉네임 변경
  @PatchMapping("/nickname")
  public ResponseEntity<UserDto> updateNickname(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody NicknameRequest request) {
    User user = userDetails.getUser();
    userService.updateNickname(request, user);
    return ResponseEntity.ok(userService.getMemberInfo(user.getUserId()));
  }

  // 프로필 사진 등록
  @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> registerProfileImage(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @ModelAttribute ProfileImageRequest request) {
    User user = userDetails.getUser();
    userService.updateProfileImage(request, user);
    return ResponseEntity.ok(userService.getMemberInfo(user.getUserId()));
  }

  // 프로필 사진 변경
  @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> updateProfile(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @ModelAttribute ProfileImageRequest request) {
    User user = userDetails.getUser();
    userService.updateProfileImage(request, user);
    return ResponseEntity.ok(userService.getMemberInfo(user.getUserId()));
  }
}
