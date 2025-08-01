package com.sqmusicplus.v3.plug.entity;

import com.sqmusicplus.v3.base.enums.PlugBrType;
import lombok.Data;

/**
 * @Classname PlugDownloadSongParam
 * @Description 下载歌曲参数
 * @Version 1.0.0
 * @Date 2025/7/28 09:40
 * @Created by SQ
 */
@Data
public class PlugDownloadSongParam extends PlugSearchMusicResult{
    /**
     * 手动选择码率
     */
    private PlugBrType brType;
}
