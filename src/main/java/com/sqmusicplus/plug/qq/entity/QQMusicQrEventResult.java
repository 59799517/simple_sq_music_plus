package com.sqmusicplus.plug.qq.entity;

import com.sqmusicplus.plug.qq.enums.QRCodeLoginEvents;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname QQMusicQrEventResult
 * @Description qq音乐监听二维码参数
 * @Version 1.0.0
 * @Date 2025/4/21 09:25
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
public class QQMusicQrEventResult {
    // 登录事件
    QRCodeLoginEvents qrCodeLoginEvents;
    // 登录事件参数
    String sigx;
    // 登录事件参数
    String uin;
    // 二维码信息
    QQMusicQr qqMusicQr;
    // 登录使用code
    String code;
    //请求url链接
    String url;


}
