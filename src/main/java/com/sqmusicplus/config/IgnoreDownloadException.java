package com.sqmusicplus.config;


/**
 * @Classname IgnoreDownloadException
 * @Description 忽略本次下载
 * @Version 1.0.0
 * @Date 2025/8/13 16:38
 * @Created by SQ
 */
public class IgnoreDownloadException extends RuntimeException {
    public IgnoreDownloadException(String message) {
        super(message);
    }
}
