package com.blogex.api.controller;

import com.blogex.api.config.AppConfig;
import com.blogex.api.request.Login;
import com.blogex.api.request.Signup;
import com.blogex.api.response.SessionResponse;
import com.blogex.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

//    private static final String KEY = "k4CJXNYNTj6PjNj6NQV/Hxm+Rdz+Z/ldEW0ROC0Ww/VbDNm/SoGPL2/TrKVG9a1D";

    /**
     * 쿠키 이용한 로그인 방법
     */
//    @PostMapping("/auth/login")
//    public ResponseEntity<Object> login(@RequestBody Login login){
//        String accessToken = authService.signIn(login);
//        // 쿠키 생성 방법
//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost") // todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30)) //쿠키 만료 시간 지정
//                .sameSite("strict")
//                .build();
//        // 쿠기가 어덯게 변한되는지 확인
//        log.info(">>>>>>> cookie={}", cookie.toString());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .build();
////        return new SessionResponse(accessToken);
//    }


    /**
     * jwt 이용한 로그인 방법
     */
    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        Long userId = authService.signIn(login);

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                // 동일한 jwt 토큰 값 안나오게 설정 (언제 발급했는지?)
                .setIssuedAt(new Date())
                .compact();

        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){

        authService.signup(signup);
    }
}
