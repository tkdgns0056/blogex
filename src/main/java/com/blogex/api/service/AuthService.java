package com.blogex.api.service;

import com.blogex.api.domain.Session;
import com.blogex.api.domain.Users;
import com.blogex.api.exception.AlreadyExistEmailException;
import com.blogex.api.exception.InvalidRequest;
import com.blogex.api.exception.InvalidSigninInformation;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Login;
import com.blogex.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signIn(Login login){
        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        // 로그인 처리 제대로 완료 되면 세션 발금
        Session session = users.addSession();

        return users.getId();
    }

    public void signup(Signup signup) {
        Optional<Users> usersOptional =  userRepository.findByEmail(signup.getEmail());
        if(usersOptional.isPresent()){
            throw new AlreadyExistEmailException();
        }

        // entity dto 변환
        var user = Users.builder()
                .name(signup.getName())
                .password(signup.getPassword())
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
