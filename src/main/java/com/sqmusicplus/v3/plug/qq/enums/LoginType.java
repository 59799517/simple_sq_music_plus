package com.sqmusicplus.v3.plug.qq.enums;


/**
 * @Classname LoginType
 * @Description 登录类型
 * @Version 1.0.0
 * @Date 2025/4/18 09:55
 * @Created by SQ
 */
public enum LoginType {
    QQ("qq"),
    WECHAT("wechat");

    private String type;

    //根据type获取枚举
    public static LoginType getByType(String type) {
        for (LoginType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

    LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
