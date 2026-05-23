package com.sqmusicplus.v3.plug.tidal.utils;

import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.plug.tidal.entity.vo.ManifestResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname TidalZarzApiTest
 * @Description 测试 Zarz API（优先访问）
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class TidalZarzApiTest {

    public static void main(String[] args) {
        log.info("========== 测试 Zarz API ==========\n");
        
        // 测试用例：使用 HI_RES_LOSSLESS 音质
        long trackId = 337502043;
        PlugBrType quality = PlugBrType.TIDAL_HI_FLAC_RES_LOSSLESS;
        
        testGetManifestResult(trackId, quality);
        
        log.info("\n========== 测试完成 ==========");
    }

    /**
     * 测试获取 ManifestResult
     */
    private static void testGetManifestResult(long trackId, PlugBrType quality) {
        log.info("【测试】获取 ManifestResult");
        log.info("TrackID: {}", trackId);
        log.info("Quality: {} ({})", quality, quality.getValue());
        log.info("");
        
        try {
            // 调用 getManifestResult（内部会先尝试 Zarz API，失败后使用代理 API）
            ManifestResult manifestResult = TidalProxyApiUtils.getManifestResult(trackId, quality);
            
            if (manifestResult == null) {
                log.error("✗ 获取 ManifestResult 失败");
                return;
            }
            
            log.info("✓ 获取 ManifestResult 成功\n");
            log.info("编码格式: {}", manifestResult.getCodecs() != null ? manifestResult.getCodecs() : "未知");
            
            if (manifestResult.isBtsFormat()) {
                log.info("格式类型: BTS (单个 URL)");
                log.info("URL: {}", manifestResult.getDirectUrl().length() > 150 ? 
                    manifestResult.getDirectUrl().substring(0, 150) + "..." : manifestResult.getDirectUrl());
                
            } else if (manifestResult.isDashFormat()) {
                log.info("格式类型: DASH (多个分段)");
                log.info("初始化段 URL: {}", manifestResult.getInitUrl());
                log.info("媒体段数量: {}", manifestResult.getMediaUrls().length);
                log.info("第一个媒体段: {}", manifestResult.getMediaUrls()[0].length() > 150 ? 
                    manifestResult.getMediaUrls()[0].substring(0, 150) + "..." : manifestResult.getMediaUrls()[0]);
                
                if (manifestResult.getM3u8Content() != null) {
                    log.info("M3U8 长度: {} 字符", manifestResult.getM3u8Content().length());
                    log.info("M3U8 预览:\n{}", manifestResult.getM3u8Content().substring(0, Math.min(500, manifestResult.getM3u8Content().length())));
                }
            }
            
        } catch (Exception e) {
            log.error("✗ 测试失败: {}", e.getMessage(), e);
        }
    }
}
