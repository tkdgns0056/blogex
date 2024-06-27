package com.blogex.api.controller;

import com.blogex.api.domain.Session;
import com.blogex.api.domain.Users;
import com.blogex.api.repositrory.PostRepository;
import com.blogex.api.repositrory.UserRepository;
import com.blogex.api.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 ")
    void test1() throws Exception {

        //given
        // 회원가입 테스트 데이터 추가
        userRepository.save(Users.builder()
                .name("짜무니")
                .email("test1@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("test1@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성 ")
    void test2() throws Exception {

        //given
        // 회원가입 테스트 데이터 추가
        Users users = userRepository.save(Users.builder()
                .name("짜무니")
                .email("test1@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("test1@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)

//        Users loggedInUser = userRepository.findById(users.getId())
//                .orElseThrow(RuntimeException::new);

        // 세션 생성된 개수 확인
        // addSession 메소드 잘 실행되는지 확인
        // 회원가입한 사용자와 로그인한 사용자의 값은 같은데  새션이 제대로 발생 했는지 확인
        Assertions.assertEquals(1L, users.getSessions().size());
    }


    @Test
    @DisplayName("로그인 성공 후 세션 응답 ")
    void test3() throws Exception {

        //given
        // 회원가입 테스트 데이터 추가
        Users users = userRepository.save(Users.builder()
                .name("짜무니")
                .email("tkdgns00562@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("tkdgns00562@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue())) // accessToken은 계속 값이 바뀌기 때문에 notNullValue 로 테스트
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다. /foo")
    void test4() throws Exception {
        //given
        Users users = Users.builder()
                .name("짜무니")
                .email("tkdgns0056@gmail.com")
                .password("1234")
                .build();
        Session session  = users.addSession();
        userRepository.save(users);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)
    }


    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속 할 수 없다.")
    void test5() throws Exception {
        //given
        Users users = Users.builder()
                .name("짜무니")
                .email("tkdgns0056@gmail.com")
                .password("1234")
                .build();
        Session session  = users.addSession();
        userRepository.save(users);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)
    }
}