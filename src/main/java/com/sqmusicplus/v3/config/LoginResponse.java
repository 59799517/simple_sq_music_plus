package com.sqmusicplus.v3.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String tokenName = "sqmusic";        // token 名称
    private String tokenValue;                   // token 值
    private Boolean isLogin = true;              // 此 token 是否已经登录
    private Object loginId;                      // 此 token 对应的 LoginId，未登录时为 null
    private String loginType = "login";          // 账号类型标识
    private Long tokenTimeout;                   // token 剩余有效期 (单位: 秒)
    private Long sessionTimeout;                 // Session 剩余有效时间 (单位: 秒)
    private String loginDevice;                  // 登录设备类型

    /**
     * 便捷构造方法
     */
    public LoginResponse(String tokenValue, Object loginId, Long tokenTimeout, String loginDevice) {
        this.tokenValue = tokenValue;
        this.loginId = loginId;
        this.tokenTimeout = tokenTimeout;
        this.sessionTimeout = tokenTimeout;
        this.loginDevice = loginDevice != null ? loginDevice : "DEFAULT";
    }
}
