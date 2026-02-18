package com.thepowermisha.document.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseDto<T> {
    private final Boolean success;
    private final T result;

    public static <T> ResponseDto<T> success(T result){
        return new ResponseDto<T>(true, result);
    }
}
