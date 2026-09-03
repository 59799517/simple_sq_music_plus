package com.sqmusicplus.v3.config.exception;


/**
 * @Classname DownloadTimeoutException
 * @Description 下载超时异常，用于标识下载超时场景，便于按异常类型拦截进入超时重试流程
 * @Version 1.0.0
 * @Created by SQ
 */
public class DownloadTimeoutException extends RuntimeException {
    public DownloadTimeoutException(String message) {
        super(message);
    }
}
