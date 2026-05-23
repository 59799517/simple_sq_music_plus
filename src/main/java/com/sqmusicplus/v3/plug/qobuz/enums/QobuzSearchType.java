package com.sqmusicplus.v3.plug.qobuz.enums;

/**
 * @Classname QobuzSearchType
 * @Description Qobuz搜索类型
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
public enum QobuzSearchType {
    TRACK("track"), ARTIST("artist"), ALBUM("album");
    
    String value;
    
    QobuzSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
