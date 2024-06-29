package com.blogex.api.service;

import com.blogex.api.domain.Users;
import com.blogex.api.exception.AlreadyExistEmailException;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Signup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1(){
        // given
        Users users = Users.builder()
                .email("tkdgns0056@gmail.com")
                .password("1234")
                .name("짬니")
                .build();
        userRepository.save(users);

        Signup signup = Signup.builder()
                .email("tkdgns0056@gmail.com")
                .password("1234")
                .name("짜무니")
                .build();

        // when
        assertThrows(AlreadyExistEmailException.class, () -> authService.signup(signup));
        // then
    }
}