package com.sqmusicplus.v3.base.entity.vo;

import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname ListenerEntity
 * @Description 解析歌曲实体类
 * @Version 1.0.0
 * @Date 2022/8/10 16:09
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
@ToString
public class ParserEntity {
    private String songName;
    private String artistsName;
//    插件名称
    private String plugName;
//    是否检测到
    private Boolean isDetection;
//  歌曲信息
    private PlugSearchMusicResult plugSearchMusicResult;
    /**
     * 下载音质格式  mp3，flac，ape,m4a，wav
     */
    private String downloadFormat;
}
