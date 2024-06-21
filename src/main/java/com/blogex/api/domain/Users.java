package com.blogex.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    /**
     * jpa 연관관계 매핑
     * CascadeType.All : casCde 속성은 부모 엔티티에서 수행된 작업이 자식 엔티티에도 영향을 미치도록함.
     * CascadeType.All은 모든 유형의 작업(예. 저장,삭제,병합,새로고침등)이 자식 엔티티에도 전파 되도록한다.
     * 즉, 현재 이 상황서는 User의 변경에 따라 Session엔티티에도 같이 영향이 간다는 뜻.
     *
     * mappedBy = "users" : users엔티티 매핑 한다는 뜻
    **/
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "users") // 유저가 1명일 때 세션은 여러개 생길수가 있다. One: 유저 Many: 세션
    private List<Session> sessions = new ArrayList<>();

    @Builder
    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Session addSession(){
        Session session  = Session.builder()
                .users(this)
                .build();
        sessions.add(session);

        return session;
    }

}
