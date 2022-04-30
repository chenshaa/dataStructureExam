package com.chensha.exam.exception;

import lombok.Getter;

@Getter
public class AuthServiceException extends RuntimeException{
    private Integer code;

    public AuthServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
