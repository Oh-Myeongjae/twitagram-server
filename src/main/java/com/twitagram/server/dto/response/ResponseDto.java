package com.twitagram.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean result;
    private T output;
    private Error status;

    public static <T> ResponseDto<T> success(T output, String code, String message) {
        return new ResponseDto<>(true, output, new Error(code, message));
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(false, null, new Error(code, message));
    }

    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }

}