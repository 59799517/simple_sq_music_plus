package com.sqmusicplus.v3.plug.apple.enums;

/**
 * @Classname DownloadMode
 * @Version 1.0.0
 * @Date 2025/10/17 16:16
 * @Created by SQ
 * 下载模式枚举类
 * 定义可用的下载工具选项
 */

public enum DownloadMode {
    /** 使用yt-dlp下载器 */
    YTDLP("ytdlp"),
    /** 使用N_m3u8DL-RE下载器 */
    NM3U8DLRE("nm3u8dlre");

    private final String value;

    /**
     * 构造函数
     * @param value 枚举对应的字符串值
     */
    DownloadMode(String value) {
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