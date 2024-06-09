package com.blogex.api.service;

import com.blogex.api.controller.response.PostResponse;
import com.blogex.api.domain.Post;
import com.blogex.api.repositrory.PostRepository;
import com.blogex.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void write(PostCreate postCreate){
        Post post =  Post.builder()
                    .title(postCreate.getTitle())
                    .content(postCreate.getContent())
                    .build();

        postRepository.save(post);

    }

//    public Post get(Long id) {
//        // 옵셔널 데이터는 바로 가져와서 쓸 수 있도록 하는것을 권장 (ex. if문으로 분기처리 할수도있지만 하지않고 이렇게 바로 처리)
////        Optional<Post> byId = postRepository.findById(id);
//            Post post = postRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
//
//            return post;
//    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        /**
         * Controller -> service -> repository 일반적인 흐름
         *
         * 다른 패턴
         * PostController -> WebPostService (response를 위해서 행위를 하는 호출은 여기서 ) -> Repository
         *                   PostService (다른 서비스(외부) 와 통신을 하기위해서 하는 부분은 여기서)
         *                   파사드 패턴 인 거 같음.
         */
    }

    public List<PostResponse> getList(Pageable pageable) {
        //pageable
//        Pageable pageable = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));

        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
