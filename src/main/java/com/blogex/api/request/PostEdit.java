package com.blogex.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostEdit {

    private String title;

    private String content;

    @Builder
    public PostEdit(String title, String content){
        this.title = title;
        this.content = content;
    }
}
