package com.arom.yeojung.service;


import com.arom.yeojung.object.User;
import com.arom.yeojung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByUsername(username);

        if (userData == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없음: " + username);
        }

        return org.springframework.security.core.userdetails.User.withUsername(userData.getUsername()) // 스프링 시큐리티에서 제공하는 User
                .password(userData.getPassword())
                .authorities(List.of())
                .build();

    }
}

