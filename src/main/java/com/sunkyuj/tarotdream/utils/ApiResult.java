package com.sunkyuj.tarotdream.utils;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ApiResult<T> {
    private final boolean success;
    private final T response;
    private final ApiError error;

    public ApiResult(boolean success, T response, ApiError error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }
}