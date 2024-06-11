package com.blogex.api.domain;

import lombok.Builder;
import lombok.Getter;

// 수정을 할 수 있는 필드들만 정의해줌
@Getter
public class PostEditor {

    private final String title;
    private final String content;

    @Builder
    public PostEditor(String title, String content){
        this.title = title;
        this.content = content;
    }
}
