package com.arom.yeojung.service;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.AuthDto;
import com.arom.yeojung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(AuthDto authDto){

        String username = authDto.getUsername();
        String password = authDto.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist){

            return;
        }

        User data = new User();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password)); // 암호화 진행 후 주입

        userRepository.save(data);
    }
}
