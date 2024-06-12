package com.blogex.api.exception;

/**
 *
 *정책상 Status Code 404
 */
public class PostNotFound extends BlogException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";


    public PostNotFound() {
        super(MESSAGE);
    }

//    public PostNotFound(Throwable cause) {
//        super(MESSAGE, cause);
//    }
}
