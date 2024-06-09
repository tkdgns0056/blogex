package com.blogex.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC) //엔티티는 기본 생성자 넣어준다.
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String title;

    @Lob
    public String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public String getTitle(){
//        // 서비스의 정책을 넣지 않는게 좋다!!! 절대!!!
//        return this.title.substring(0,10);
//    }
}
