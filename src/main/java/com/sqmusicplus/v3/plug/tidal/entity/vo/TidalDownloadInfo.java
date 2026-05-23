package com.sqmusicplus.v3.plug.tidal.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname TidalDownloadInfo
 * @Description Tidal 下载信息（对应 Go 版本的 TidalDownloadInfo）
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TidalDownloadInfo {
    
    /**
     * 下载 URL（可能是直接 URL 或 MANIFEST: 前缀的 Base64 编码）
     */
    private String url;
    
    /**
     * 位深度（bit），如 16、24
     */
    private int bitDepth;
    
    /**
     * 采样率（Hz），如 44100、96000
     */
    private int sampleRate;
    
    /**
     * 判断是否为 Manifest 格式
     */
    public boolean isManifest() {
        return url != null && url.startsWith("MANIFEST:");
    }
    
    /**
     * 获取 Manifest Base64 内容（去除 MANIFEST: 前缀）
     */
    public String getManifestBase64() {
        if (isManifest()) {
            return url.substring("MANIFEST:".length());
        }
        return null;
    }
}
