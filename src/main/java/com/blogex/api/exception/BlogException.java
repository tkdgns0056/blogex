package com.blogex.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * customException class
 */
@Getter
public abstract class BlogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();

    public BlogException(String message) {
        super(message);
    }

    public BlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }

}
