package com.blogex.api.controller;

import com.blogex.api.controller.response.ErrorResponse;
import com.blogex.api.exception.BlogException;
import com.blogex.api.exception.InvalidRequest;
import com.blogex.api.exception.PostNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody // 핸들러에서 ResponseBody 달아주면 json 형태로넘겨줌
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

            ErrorResponse response = ErrorResponse.builder()
                    .code("400")
                    .message("잘못된 요청입니다.")
                    .build();

            for(FieldError fieldError : e.getFieldErrors()){
                response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return response;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e){
        ErrorResponse response = ErrorResponse.builder()
                .code("404")
                .message("존재하지 않는 글입니다.")
                .build();

        return response;
    }

    // 예외 처리가 늘어날 떄마다 예외 메소드를 만들어서 처리할것인가? 너무 힘듦
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogException.class)
    public ErrorResponse blogException(BlogException e){
        ErrorResponse response = ErrorResponse.builder()
                .code("404")
                .message("존재하지 않는 글입니다.")
                .build();

        return response;
    }
}
