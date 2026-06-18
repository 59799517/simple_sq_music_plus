package com.sqmusicplus.v3.plug.entity;

import lombok.Data;

/**
 * @Classname PlugDownloadAlbumParam
 * @Description 下载专辑参数
 * @Version 1.0.0
 * @Date 2025/7/28 17:25
 * @Created by SQ
 */
@Data
public class PlugDownloadAlbumParam extends PlugSearchAlbumResult{
    /**
     * 手动选择码率（空则自动适配最高音质）
     */
    private Integer bit;
    /**
     * 下载音质格式  mp3，flac，ape,m4a，wav
     */
    private String downloadFormat;
}
