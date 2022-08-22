package com.twitagram.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@Getter
public class ResponseDto<T> {
    private boolean result;
    private T output;
    private Error status;

    public static <T> ResponseDto<T> success(T output) {
        return new ResponseDto<>(true, output, null);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(false, null , new Error(code, message));
    }

    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }
}
