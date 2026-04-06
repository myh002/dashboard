package com.dashboard.common;

import com.dashboard.common.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResultData<T> success(T data) {
        return new ResultData<>(ResultCode.SUCCESS, "success", data);
    }

    public static <T> ResultData<T> success() {
        return new ResultData<>(ResultCode.SUCCESS, "success", null);
    }

    public static <T> ResultData<T> error(String message) {
        return new ResultData<>(ResultCode.ERROR, message, null);
    }

    public static <T> ResultData<T> error(int code, String message) {
        return new ResultData<>(code, message, null);
    }

    public static <T> ResultData<T> error() {
        return new ResultData<>(ResultCode.ERROR, "error", null);
    }
}
