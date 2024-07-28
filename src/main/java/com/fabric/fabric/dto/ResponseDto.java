package com.fabric.fabric.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private int code;// 非0表示失败
    private String message;// 错误信息
    private T data;

    public static <D> ResponseDto<D> success(D data) {
        ResponseDto<D> responseDto = new ResponseDto<>();
        responseDto.setCode(0);
        responseDto.setData(data);
        return responseDto;
    }

    public static ResponseDto<Void> success() {
        return success(null);
    }

    public static <D> ResponseDto<D> fail(Integer code, String message) {
        ResponseDto<D> responseDto = new ResponseDto<>();
        responseDto.setCode(code);
        responseDto.setMessage(message);
        return responseDto;
    }

    public static <D> ResponseDto<D> fail(String message) {
        return fail(1, message);
    }
}
