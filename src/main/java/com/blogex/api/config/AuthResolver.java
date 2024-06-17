package com.blogex.api.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Http Header, Session, Cookie 등 직접적이지 않는 방식 혹은 외부 데이터 스토어로부터 바인딩이 이뤄져야 하는 경우가 있다.
 * 그래서 해결해주는 역할로 Argument Resolver를 사용한다.
 */
public class AuthResolver implements HandlerMethodArgumentResolver {

    // 나중에 컨트롤러에서 사용 할 dto나 어노테이션이 지원하는거야? 요청한 라우터가 넘어왔을때
    // 사용자가 정말 원하는 dto인지 물어보는 용도
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }
}
