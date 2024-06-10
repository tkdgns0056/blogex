package com.blogex.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default // 특정 필드를 초기화 할 때 사용. 지금은 페이지를 1로 초기화./ 클래스에 bulider 달아야 작동 하는듯
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    public long getOffset(){
        return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZE);
    }
}
