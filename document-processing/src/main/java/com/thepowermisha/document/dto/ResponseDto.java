package com.thepowermisha.document.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseDto<T> {
    private final Boolean success;
    private final T result;
    private final ExceptionDto error;

    public static <T> ResponseDto<T> success(T result){
        return new ResponseDto<>(true, result, null);
    }

    public static ResponseDto<Void>  error(String code, String message) {
        return new ResponseDto<>(false, null ,new ExceptionDto(code, message));
    }
}
