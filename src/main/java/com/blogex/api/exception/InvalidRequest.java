package com.blogex.api.exception;

/**
 * 정책상 InvalidRequest 400으로
 */
public class InvalidRequest extends BlogException{

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest(){
        super(MESSAGE);
    }

    public abstract int statusCode(){

    }
}
