package com.sqmusicplus.v3.plug.tidal.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname ManifestResult
 * @Description Manifest 解析结果（支持 BTS 和 DASH 格式）
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManifestResult {
    
    /**
     * BTS 格式的直接下载 URL
     */
    private String directUrl;
    
    /**
     * DASH 格式的初始化段 URL
     */
    private String initUrl;
    
    /**
     * DASH 格式的媒体段 URLs 数组
     */
    private String[] mediaUrls;
    
    /**
     * 音频编解码器信息
     */
    private String codecs;
    
    /**
     * M3U8 播放列表内容（仅 DASH 格式）
     */
    private String m3u8Content;
    
    /**
     * 原始 MPD XML 内容（仅 DASH 格式）
     */
    private String originalMpdXml;
    
    /**
     * 判断是否为 BTS 格式（有直接 URL）
     */
    public boolean isBtsFormat() {
        return directUrl != null && !directUrl.isEmpty();
    }
    
    /**
     * 判断是否为 DASH 格式（有分段 URLs）
     */
    public boolean isDashFormat() {
        return initUrl != null && !initUrl.isEmpty() && mediaUrls != null && mediaUrls.length > 0;
    }
    
    /**
     * 获取分段数量
     */
    public int getSegmentCount() {
        return mediaUrls != null ? mediaUrls.length : 0;
    }
}
