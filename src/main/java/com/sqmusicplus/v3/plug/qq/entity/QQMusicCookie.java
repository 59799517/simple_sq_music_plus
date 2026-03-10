package com.sqmusicplus.v3.plug.qq.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname QQMusicCookieInfo
 * @Description qq音乐cookie信息
 * @Version 1.0.0
 * @Date 2025/4/28 17:52
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
public class QQMusicCookie {

    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "ts")
    private Long ts;
    @JSONField(name = "start_ts")
    private Long startTs;
    @JSONField(name = "traceid")
    private String traceid;
    @JSONField(name = "req")
    private ReqDTO req;
    @JSONField(name = "req1")
    private ReqDTO req1;


@Data
    public static class ReqDTO {
        @JSONField(name = "code")
        private Integer code;
        @JSONField(name = "data")
        private QQMusicCookieInfo data;

    }
}
