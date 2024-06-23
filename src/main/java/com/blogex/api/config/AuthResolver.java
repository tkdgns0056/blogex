package com.blogex.api.config;

import com.blogex.api.config.data.UserSession;
import com.blogex.api.domain.Session;
import com.blogex.api.exception.Unauthorized;
import com.blogex.api.repositrory.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.http.HttpRequest;


/**
 * Http Header, Session, Cookie 등 직접적이지 않는 방식 혹은 외부 데이터 스토어로부터 바인딩이 이뤄져야 하는 경우가 있다.
 * 그래서 해결해주는 역할로 Argument Resolver를 사용한다.
 */
@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    // 나중에 컨트롤러에서 사용 할 dto나 어노테이션이 지원하는거야? 요청한 라우터가 넘어왔을때
    // 사용자가 정말 원하는 dto인지 물어보는 용도
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class); //
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class); // getPamaeter로 가져오면 충돌이 발생할 수 있으므로 header에 담아서 넣는다.
        if(servletRequest == null){
            log.info("servletRequest null");
            throw new Unauthorized();
        }

        Cookie[] cookies = servletRequest.getCookies();

        if(cookies.length == 0){
            log.error("쿠키가 없음");
            throw new Unauthorized();
        } // 여기까진 쿠키가 있음.

        String accessToken = cookies[0].getValue();


        // 데이터베이스 사용자 확인작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return new UserSession(session.getUsers().getId());
    }
}