package com.blogex.api.service;

import com.blogex.api.crypto.PasswordEncoder;
import com.blogex.api.domain.Users;
import com.blogex.api.exception.AlreadyExistEmailException;
import com.blogex.api.exception.InvalidSigninInformation;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Login;
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
        Signup signup = Signup.builder()
                .email("tkdgns0056@gmail.com")
                .password("1234")
                .name("짜무니")
                .build();

        // when
        authService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        Users users = userRepository.findAll().iterator().next();
        assertEquals("tkdgns0056@gmail.com", users.getEmail());
        assertNotNull(users.getPassword());
        assertNotEquals("1234", users.getPassword());
        assertEquals("짜무니", users.getName());
    }

    @Test
    @DisplayName("회원가입 시 이메일은 중복될 수 없다.")
    void test2(){
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

    @Test
    @DisplayName("로그인 성공")
    void test3(){
        // given
        PasswordEncoder encoder = new PasswordEncoder();
        String encryptedPassword = encoder.encrpyt("1234");

        Users users = Users.builder()
                .email("tkdgns0056@gmail.com")
                .password(encryptedPassword)
                .name("짜무니")
                .build();
        userRepository.save(users);

        Login login = Login.builder()
                .email("tkdgns0056@gmail.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signIn(login);

        // then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4(){
        // given
        PasswordEncoder encoder = new PasswordEncoder();
        String encryptedPassword = encoder.encrpyt("1234");

        Users users = Users.builder()
                .email("tkdgns0056@gmail.com")
                .password(encryptedPassword)
                .name("짜무니")
                .build();
        userRepository.save(users);

        Login login = Login.builder()
                .email("tkdgns0056@gmail.com")
                .password("5678")
                .build();

        // expected
        assertThrows(InvalidSigninInformation.class,
                () -> authService.signIn(login));
    }
}