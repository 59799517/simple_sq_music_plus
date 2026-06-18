package com.sqmusicplus.v3.plug.entity;

import lombok.Data;

/**
 * @Classname PlugDownloadArtisParam
 * @Description 下载歌手参数
 * @Version 1.0.0
 * @Date 2025/7/28 17:15
 * @Created by SQ
 */
@Data
public class PlugDownloadArtisParam extends PlugSearchArtistResult {
    /**
     * 手动选择码率
     */
    private Integer bit;
    /**
     * 下载音质格式  mp3，flac，ape,m4a，wav
     */
    private String downloadFormat;

}
