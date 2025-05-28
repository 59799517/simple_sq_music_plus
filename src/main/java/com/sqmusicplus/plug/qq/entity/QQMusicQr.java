package com.sqmusicplus.plug.qq.entity;

import com.sqmusicplus.plug.qq.enums.LoginType;
import lombok.Data;

/**
 * @Classname QQMusicQr
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/4/18 09:58
 * @Created by SQ
 */
@Data
public class QQMusicQr {
    private byte[] data;
    private LoginType qrType;
    private String qrTypeStr;
    private String mimeType;
    private String identifier;
    //重试次数
    private Integer retryCount;

    public QQMusicQr(byte[] data, LoginType qrType, String mimeType, String identifier, Integer retryCount) {
        this.data = data;
        this.qrType = qrType;
        this.qrTypeStr = qrType.getType();
        this.mimeType = mimeType;
        this.identifier = identifier;
        this.retryCount = retryCount;
    }
}
