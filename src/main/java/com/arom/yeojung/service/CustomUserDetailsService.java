package com.arom.yeojung.service;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.user.CustomUserDetails;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.error("사용자를 찾을 수 없습니다. 회원 Username: {}", username);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    // CustomUserDetails 사용 시
    return new CustomUserDetails(user);

    // Spring Security 기본 User 사용 시 (CustomUserDetails 사용 안 할 경우)
        /*
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of()) // 권한이 있으면 추가
                .build();
        */
  }
}
