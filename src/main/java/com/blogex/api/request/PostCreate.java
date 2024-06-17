package com.blogex.api.request;

import com.blogex.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;


    // 빌더의장점
    // - 가독성이 좋다. (값 생성에 대한 유연함)
    // - 필요한 값만 받을 수 있다. -> 오버로딩 가능한 조건 찾아보기..
    // - 객체의 불변성(제일 중요)
    @Builder // 생성자에 빌더를 달아주는 것을 추천.
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // request 객체에서 특정 유효성에 대해 미리 메소드 만들고 검사
    public void validate(){
        if(title.contains("바보")){
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}


