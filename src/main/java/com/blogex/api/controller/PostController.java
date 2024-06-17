package com.blogex.api.controller;

import com.blogex.api.config.data.UserSession;
import com.blogex.api.controller.response.PostResponse;
import com.blogex.api.domain.Post;
import com.blogex.api.exception.InvalidRequest;
import com.blogex.api.request.PostCreate;
import com.blogex.api.request.PostEdit;
import com.blogex.api.request.PostSearch;
import com.blogex.api.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE

    private final PostService postService;

    @GetMapping("/test")
    public String test(){
        return "hello";
    }

    @GetMapping("/foo")
    public String foo(UserSession userSession){
        log.info(">>>{}", userSession.name);
        return "foo";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request, @RequestHeader String authorization){
        // 검증 어노테이션으로 검증이 힘들 경우 이렇게 사용도 한다.
//        if(request.getTitle().contains("바보")){
//            throw new InvalidRequest();
//        }

        // 간단한 사용자 인증 방식
        if(authorization.equals("zzamuni")){
            request.validate();
            postService.write(request);
        }

        // Case1. 저장한 데이터 Entity -> response로 응답하기
//        return postService.write(request);
        // Case2. 저장한 데이터에 primary_id -> response로 응답하기
        //        Client에서는 수신한 id를 post 조회 API를 통해서 글 데이터를 수신받음.

        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
        // Bad Case: 서버에서 -> 반드시 이렇게 할겁니다! fix
        //          -> 서버세엇는 차라리 유연하게 대응하는게 좋다. -> 코드 잘 짜야됨.
        //          -> 한번에 일괄적으로 잘 처리되는 케이스는 없다. -> 잘 관리하는 형태가 중요
//        Long postId = postService.write(request);
////        return Map.of("postId", postId);
        postService.write(request);
    }
    /**
     * 조회 API
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name= "postId") Long id){
        // Request  클래스 (PostRequest -> 요청 및 Validation 정책)
        // Response 클래스를 명확하게 나눔. ( 서비스 정책에 맞는 로직이 들어갈 수 있게 response객체 만듦)
        return postService.get(id);
    }
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch){
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable(name= "postId") Long postId, @RequestBody @Valid PostEdit request, @RequestHeader String authorization){
        if(authorization.equals("zzamuni")){
            postService.edit(postId, request);
        }

    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId, @RequestHeader String authorization){
        if(authorization.equals("zzamuni")){
            postService.delete(postId);
        }
    }
}
