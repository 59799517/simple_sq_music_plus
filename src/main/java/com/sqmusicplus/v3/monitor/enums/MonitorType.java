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
    PLAYLIST("歌单", 1),
    //歌手
    SINGER("歌手", 2),
    //专辑
    ALBUM("专辑", 3),
    //歌曲
    SONG("歌曲", 4),
    //用户
    USER("用户", 5);
    private String name;
    private int value;

    MonitorType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
