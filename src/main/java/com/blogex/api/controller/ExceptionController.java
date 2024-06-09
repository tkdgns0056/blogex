package com.blogex.api.controller;

import com.blogex.api.controller.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

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
}
