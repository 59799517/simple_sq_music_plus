package com.sqmusicplus.v3.base.entity.vo;

import com.sqmusicplus.v3.base.enums.PlugBrType;
import lombok.Data;

/**
 * @Classname DownloadMusicParam
 * @Description 下载音频参数
 * @Version 1.0.0
 * @Date 2025/7/25 10:18
 * @Created by SQ
 */
@Data
public class DownloadMusicParam {
    /**
     * 搜索插件名称
     */
    private String plugName;
    /**
     * 搜索的ID
     */
    private String id;
    /**
     * 码率  目前 128 320 2000 这三种都能获取
     */
    private Integer bit;
}
