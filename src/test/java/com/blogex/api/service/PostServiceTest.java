package com.blogex.api.service;

import com.blogex.api.controller.response.PostResponse;
import com.blogex.api.domain.Post;
import com.blogex.api.repositrory.PostRepository;
import com.blogex.api.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        postService.write(postCreate);

        // when
        postService.write(postCreate);

        // then (데이터 검증)
        Assertions.assertEquals(2L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse post = postService.get(requestPost.getId());

        // then
        Assertions.assertNotNull(post);
        Assertions.assertEquals(1L, postRepository.count());
        Assertions.assertEquals("foo", post.getTitle());
        Assertions.assertEquals("bar", post.getContent());
    }


    @Test
    @DisplayName("글 1페이지 조회")
    void test3(){
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                            .title("짜무니 제목 " + i)
                            .content("포레스티아 " + i)
                            .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));

        // when
        List<PostResponse> posts = postService.getList(pageable);

        // then
//        Assertions.assertEquals(2L, posts.size());
        Assertions.assertEquals(5L, posts.size());
        Assertions.assertEquals("짜무니 제목 30", posts.get(0).getTitle());
        Assertions.assertEquals("짜무니 제목 26", posts.get(4).getTitle());
    }
}