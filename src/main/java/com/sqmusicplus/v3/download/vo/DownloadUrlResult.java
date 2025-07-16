package com.sqmusicplus.v3.download.vo;

import lombok.Data;

import java.util.HashMap;

/**
 * @Classname DownloadUrlResult
 * @Description 下载链接返回值
 * @Version 1.0.0
 * @Date 2025/7/14 15:12
 * @Created by SQ
 */
@Data
public class DownloadUrlResult {
    /**
     * 下载链接
     */
    private String url;
    /**
     * 如果有错误的错误消息
     */
    private String errorMsg;
    /**
     * 最终使用的插件
     */
    private String plugBrTypeId;
    /**
     * 码率
     */
    private String bit;
    /**
     * 其他补充参数（例如QQ 的ekey vkey等）
     */
    public HashMap<String, String> otherData;
}
