package com.sqmusicplus.v3.download;

/**
 * @Classname DownloadStatus
 * @Description 下载状态
 * @Version 1.0.0
 * @Date 2023/8/23 15:00
 * @Created by SQ
 */
public enum DownloadStatus {


    waiting("待下载","waiting"),
    loading("正在进行","loading"),
    
    success("成功","success"),

    error("错误","error");

    String name;
    String value;

    DownloadStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
