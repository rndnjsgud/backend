package com.arom.yeojung.controller;

import com.arom.yeojung.object.dto.AuthDto;
import com.arom.yeojung.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody AuthDto authDto) {
        authService.join(authDto);
        return ResponseEntity.ok("회원가입 성공");
    }

}