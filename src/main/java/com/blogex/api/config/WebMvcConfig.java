package com.blogex.api.config;

import com.blogex.api.repositrory.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionRepository sessionRepository;

//    @Override
////    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(new AuthInterceptor())
////                .excludePathPatterns("..", "/") // 스프링에서 기본적으로 담겨져있는 라우터 경로
////                .excludePathPatterns("/error", "favicon.ico");
////
////    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(sessionRepository));
    }
}