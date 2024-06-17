package com.blogex.api.service;

import com.blogex.api.controller.response.PostResponse;
import com.blogex.api.domain.Post;
import com.blogex.api.domain.PostEditor;
import com.blogex.api.exception.PostNotFound;
import com.blogex.api.repositrory.PostRepository;
import com.blogex.api.request.PostCreate;
import com.blogex.api.request.PostEdit;
import com.blogex.api.request.PostSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(PostNotFound::new);

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

    public List<PostResponse> getList(PostSearch postSearch) {
        //pageable
//        Pageable pageable = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
//        postRepository.save(post);// 필요없음 메서드위에 트랜잭션 설정하면 알아서 함.
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
