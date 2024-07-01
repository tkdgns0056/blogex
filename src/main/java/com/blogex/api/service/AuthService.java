package com.blogex.api.service;

import com.blogex.api.crypto.PasswordEncoder;
import com.blogex.api.domain.Session;
import com.blogex.api.domain.Users;
import com.blogex.api.exception.AlreadyExistEmailException;
import com.blogex.api.exception.InvalidRequest;
import com.blogex.api.exception.InvalidSigninInformation;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Login;
import com.blogex.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
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
//        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);

        Users users = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        PasswordEncoder encoder = new PasswordEncoder();
        // 평문, 암호화 비밀번호 확인 해주는 메서드(matches)
        boolean matches = encoder.matches(login.getPassword(), users.getPassword());

        if(!matches){
            throw new InvalidSigninInformation();
        }

        return users.getId();
    }

    public void signup(Signup signup) {
        Optional<Users> usersOptional =  userRepository.findByEmail(signup.getEmail());
        if(usersOptional.isPresent()){
            throw new AlreadyExistEmailException();
        }

        PasswordEncoder encoder = new PasswordEncoder();

        String encryptedPassword = encoder.encrpyt(signup.getPassword());

        // entity dto 변환
        var user = Users.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
