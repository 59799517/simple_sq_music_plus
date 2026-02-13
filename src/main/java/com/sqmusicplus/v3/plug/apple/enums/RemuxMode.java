package com.sqmusicplus.v3.plug.apple.enums;


/**
 * @Classname RemuxMode
 * @Version 1.0.0
 * @Date 2025/10/17 16:17
 * @Created by SQ
 * 重封装模式枚举类
 * 定义可用的媒体文件重封装工具
 */
public enum RemuxMode {
    /** 使用FFmpeg进行重封装 */
    FFMPEG("ffmpeg"),
    /** 使用MP4Box进行重封装 */
    MP4BOX("mp4box");

    private final String value;

    /**
     * 构造函数
     * @param value 枚举对应的字符串值
     */
    RemuxMode(String value) {
        this.value = value;
    }

    /**
     * 获取枚举值
     * @return 对应的字符串值
     */
    public String getValue() {
        return value;
    }
}