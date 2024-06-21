package com.blogex.api.service;

import com.blogex.api.domain.Session;
import com.blogex.api.domain.Users;
import com.blogex.api.exception.InvalidSigninInformation;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signIn(Login login){
        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        // 로그인 처리 제대로 완료 되면 세션 발금
        Session session = users.addSession();

        return session.getAccessToken();
    }
}
