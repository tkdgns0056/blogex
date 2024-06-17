package com.blogex.api.service;

import com.blogex.api.controller.response.PostResponse;
import com.blogex.api.domain.Post;
import com.blogex.api.exception.PostNotFound;
import com.blogex.api.repositrory.PostRepository;
import com.blogex.api.request.PostCreate;
import com.blogex.api.request.PostEdit;
import com.blogex.api.request.PostSearch;
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

        // when
        postService.write(postCreate);

        // then (데이터 검증)
        assertEquals(1L, postRepository.count());
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
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }


    @Test
    @DisplayName("글 여러개 조회")
    void test3(){
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                            .title("foo" + i)
                            .content("bar " + i)
                            .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10) //화면 정책에 따라서 달라질 수 있음(ex. 10개씩 더 보기나 게시글 페이지마다 바로 10개씩 등)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
//        Assertions.assertEquals(2L, posts.size());
        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목을 수정")
    void test4(){
        // given
     Post post = Post.builder()
             .title("짜무니")
             .content("포레스티아")
             .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("짜무니")
                .content("초가집")
                .build();
        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다 id=" + post.getId()));
        assertEquals("초가집", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test5(){

        // given
        Post post = Post.builder()
                .title("짜무니")
                .content("포레스티아")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        // 삭제했을때 게시글이 0이냐?
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test7(){
        // given
        Post post = Post.builder()
                .title("짜무니")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
//        IllegalArgumentException e e= assertThrows(IllegalArgumentException.class, () -> {
//            postService.get(post.getId() + 1L);
//        });e
//        //, "예외 처리가 잘못 되었어요."); // 만약에 예외처리가 실패한 경우 이 메시지를 출력
//
//        Assertions.assertEquals("존재하지 않는 글입니다.", e.getMessage());

        // 위에 예외처리 코드를 클래스와 시켜서 공통으로 사용 할 수 있게 변경
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재 하지 않는 글")
    void test8(){
        // given
        Post post = Post.builder()
                .title("짜무니")
                .content("포레스티아")
                .build();

        postRepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });

    }


    @Test
    @DisplayName("글 제목을 수정 - 존재하지 않는 글")
    void test9(){
        // given
        Post post = Post.builder()
                .title("짜무니")
                .content("포레스티아")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("짜무니")
                .content("초가집")
                .build();

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1, postEdit);
        });
    }
}