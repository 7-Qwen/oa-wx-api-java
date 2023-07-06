package com.wen.oawxapi.common.exception;

import lombok.Data;

/**
 * @author: 7wen
 * @Date: 2023-05-18 18:59
 * @description:
 */
@Data
public class CustomException extends RuntimeException {
    private String msg;
    private int code = 500;

    public CustomException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CustomException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
    }

    public CustomException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CustomException(String msg, Throwable e, int code) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
