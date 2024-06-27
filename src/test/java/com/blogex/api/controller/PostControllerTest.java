package com.blogex.api.controller;

import com.blogex.api.response.PostResponse;
import com.blogex.api.domain.Post;
import com.blogex.api.repositrory.PostRepository;
import com.blogex.api.request.PostCreate;
import com.blogex.api.request.PostEdit;
import com.blogex.api.response.PostResponse;
import com.blogex.api.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest // MockMvc 테스트를 위해 어노테이션 설정 (간단한 웹 레이어 테스트 정도는 괜찮음)
@SpringBootTest      // 지금은 서비스와 레파지토리도 만들었기 떄문에 어플리케이션 전반적인 테스트를 진행하였다. 그래서 @WebMvcTest가아니라 @SpringBootRest로 작성한다.
@AutoConfigureMockMvc // WebMvcTest를 @SpringBootTest와 같이 쓸수가 없다. 그러면 결국 mockMvc를 사용할 수 없는데 이 어노테이션을 통해서 해결한다.
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 오브젝트매퍼에 대한 빈을 주입 받을 수 있음.

    @Autowired
    private MockMvc mockMvc; // Could not autowire. No beans of 'MockMvc' type found.

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();;
    }

    @Test
    @DisplayName("글 작성 시 Hello World를 출력한다.")
    void test() throws Exception {

        //given
        /* postcreate를 생성자로 만들었는데, 나중에 개발자가 필드 순서를 바꾸면?.. 생성자를 이용하는 이 부분을 보면 순서가 뒤바뀌면 문제가 생김
        * 이런 문제를 해결하기 빌더 패턴 사용
        * */
//        PostCreate request = new PostCreate("제목입니다.", "내용입니다.");
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request); // 자바 빈 룰을 따라서 json형태로 가공을 해준다.(getter 받은것 필드들)

        // 데이터가 실제로 json으로 잘 생성이 되는지 확인
        System.out.println(json);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)
    }

    @Test
    @DisplayName("/글 작성 요청시 title값은 필수다.")
    void test2() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)
    }

    @Test
    @DisplayName("글 작성 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when (이런 요청을 했을때)
        mockMvc.perform(post("/posts?authorization=zzz")
                        .header("authorization", "mmagi")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print()); // http 요청에 대한 써머리를 남겨줌 (header, contentType 등등 표시)

        // then (이런 결과가 나온다.)
        // Assertions를 통해 기대하는 값이 1이여서 기대값이 1개라고 생각한다.
        assertEquals(2L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post request = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(request);

        // when
        PostResponse response = postService.get(request.getId());

        // 클라이언트 요구사항
        // json응답에서 title값 길이를 최대 10글자로 해주세요.


        // 버그 발생 케이스 1
        // given에서 요청이 5글자 12345만 왔다고 가정, 에러 발생 PostResponse에서 빌더 생성자에서 해당 유효성 미리 체크


        // expected (when + then)
        mockMvc.perform(get("/posts/{postId}", response.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected (when + then)
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("[0].title").value("foo19"))
                .andExpect(jsonPath("[0].content").value("bar19"))
                .andDo(print());
    }


    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected (when + then)
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("[0].title").value("foo19"))
                .andExpect(jsonPath("[0].content").value("bar19"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("짜무니")
                .content("포레스티아")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("짬니")
                .content("포레스티아")
                .build();
        // when
        postService.edit(post.getId(), postEdit);

        // expected (when + then)
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )

                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("짜무니")
                .content("포레스티아")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception{
        // 존재하지 않는 아이디로 조회
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception{
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("짜무니")
                .content("반포자이")
                .build();

        // 존재하지 않는 아이디로 조회
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        // writeValueAsString(value) : values:String 타입으로 변환할 대상
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("반포자이")
                .build();

        //writeValueAsString: request  객체를 JSON 문자열로 변환
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}