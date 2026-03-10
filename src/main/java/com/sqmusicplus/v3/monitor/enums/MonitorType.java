package com.sqmusicplus.v3.monitor.enums;


/**
 * @Classname MonitorType
 * @Description
 * @Version 1.0.0
 * @Date 2026/3/2
 * @Created by SQ
 */
public enum MonitorType {

    //歌单
    PLAYLIST("歌单", 1,"playlist"),
    //歌手
    SINGER("歌手", 2,"singer"),
    //专辑
    ALBUM("专辑", 3,"album"),
    //歌曲
    SONG("歌曲", 4,"song"),
    //用户
    USER("用户", 5,"user");
    private String name;
    private int value;
    private String code;

    MonitorType(String name, int value, String code) {
        this.name = name;
        this.value = value;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
