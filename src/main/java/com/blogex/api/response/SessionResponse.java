package com.blogex.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionResponse {

    private final String accessToken;

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
