package com.blogex.api.controller;

import com.blogex.api.domain.Users;
import com.blogex.api.exception.InvalidSigninInformation;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Login;
import com.blogex.api.response.SessionResponse;
import com.blogex.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/auth/login")
//    public Users login(@RequestBody Login login){
//        // json 아이디/비밀번호
//        log.info(">>>login ={}", login);
//
//        // DB에서 조회
//        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);
//
//        // 토큰을 응답
//        return users;
//
//    }

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
       String accessToken = authService.signIn(login);
       return new SessionResponse(accessToken);
    }
}
