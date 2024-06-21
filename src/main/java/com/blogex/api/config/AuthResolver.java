package com.blogex.api.config;

import com.blogex.api.config.data.UserSession;
import com.blogex.api.exception.Unauthorized;
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
        return parameter.getParameterType().equals(UserSession.class); //
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization"); // getPamaeter로 가져오면 충돌이 발생할 수 있으므로 header에 담아서 넣는다.
        if(accessToken == null || accessToken.equals("")){
            throw new Unauthorized();
        }

        // 데이터베이스 사용자 확인작업
        // ...

        return new UserSession(1L);
    }
}
