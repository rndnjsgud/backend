package com.arom.yeojung.service;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.user.CustomUserDetails;
import com.arom.yeojung.object.dto.user.ProfileImageRequest;
import com.arom.yeojung.object.dto.user.UserDto;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final S3Uploader s3Uploader;
  private final UserRepository userRepository;
  private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB 제한

  // Id로 회원 정보 조회
  @Transactional(readOnly = true)
  public UserDto getMemberInfo(Long userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> {
          log.error("회원 조회 실패: userId: {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    log.info("회원 정보 조회 성공: userId: {}", userId);
    return UserDto.builder()
        .username(user.getUsername())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }

  // 닉네임 변경
  @Transactional
  public void updateNickname(String nickname, User user) {
    // 닉네임 중복 체크
    if (userRepository.existsByNickname(nickname)) {
      throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
    }

    user.setNickname(nickname);
    userRepository.save(user);
    updateSecurityContext(user);
    log.info("닉네임을 변경하였습니다. nickname: {}", user.getNickname());
  }

  @Transactional(readOnly = true)
  public Boolean checkNicknameDuplicate(String nickname) {
    // 닉네임 중복 체크
    log.info("이미 사용중인 닉네임입니다.");
    return !userRepository.existsByNickname(nickname);
  }

  // 프로필 사진 업로드 (등록 & 변경)
  @Transactional
  public void updateProfileImage(ProfileImageRequest request, User user) {
    MultipartFile profileImage = request.getProfileImage();

    // 파일 크기 제한 검사
    if (profileImage.getSize() > MAX_FILE_SIZE) {
      log.error("파일 크기가 20MB를 초과했습니다: fileSize={}", profileImage.getSize());
      throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
    }

    // 기존 프로필 이미지 삭제 (S3에 존재하는지 확인 후 삭제)
    String currentProfileImageUrl = user.getProfileImageUrl();
    if (currentProfileImageUrl != null) {
      s3Uploader.deleteFile(currentProfileImageUrl);
      log.info("기존 프로필 사진을 삭제하였습니다. profileImageUrl: {}", currentProfileImageUrl);
    }

    // 새 프로필 사진 업로드
    String imageUrl = s3Uploader.uploadFile(profileImage, "profile");

    // DB에 업데이트 (등록 or 변경)
    user.setProfileImageUrl(imageUrl);
    userRepository.save(user);

    // SecurityContext 업데이트
    updateSecurityContext(user);

    log.info("프로필 사진을 업로드하였습니다. profileImageUrl: {}", user.getProfileImageUrl());
  }

  // CustomUserDetails 업데이트
  private void updateSecurityContext(User user) {
    CustomUserDetails updatedUserDetails = new CustomUserDetails(user);

    Authentication newAuth = new UsernamePasswordAuthenticationToken(
        updatedUserDetails, null, updatedUserDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(newAuth);
  }
}
