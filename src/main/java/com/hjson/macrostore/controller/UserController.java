package com.hjson.macrostore.controller;

import com.hjson.macrostore.core.auth.session.MyUserDetails;
import com.hjson.macrostore.core.exception.Exception401;
import com.hjson.macrostore.dto.user.UserRequest;
import com.hjson.macrostore.model.user.User;
import com.hjson.macrostore.model.user.UserRepository;
import com.hjson.macrostore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static UserRepository userRepository;

    private static UserService userService;

    // 로그인시 액세스 토큰, 리프레쉬 토큰 응답 필요.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDTO loginDTO,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails, Errors errors) {

        User userPS = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(()-> new Exception401("아이디를 찾을 수가 없습니다."));



    }

}
