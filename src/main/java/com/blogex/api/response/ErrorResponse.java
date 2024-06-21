package com.blogex.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code" : "400",
 *     "message" : "잘못된 요청입니다.",
 * }
 *
 * @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
 * - null, absent, Colletions, Map의 isEmpty()가 true, Array의 length 0, String length 0인 데이터 제외
 */
@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 비어있는 값도 넘길 필요가 있기떄문에 선호x
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
