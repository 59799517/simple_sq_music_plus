package com.sqmusicplus.v3.config.exception;

/**
 * @Classname SQException
 * @Description 标准异常
 * @Version 1.0.0
 * @Date 2025/7/24 17:46
 * @Created by SQ
 */

public class SQException extends RuntimeException{
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public SQException(String msg) {
        this.msg = msg;
    }
}
