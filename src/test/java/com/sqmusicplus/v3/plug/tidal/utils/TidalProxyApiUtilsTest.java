package com.sqmusicplus.v3.plug.tidal.utils;

import com.sqmusicplus.v3.plug.tidal.entity.vo.ManifestResult;
import com.sqmusicplus.v3.plug.tidal.entity.vo.TidalDownloadInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname TidalProxyApiUtilsTest
 * @Description TidalProxyApiUtils 使用示例和测试
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class TidalProxyApiUtilsTest {

    /**
     * 完整的下载流程示例（最简化版）
     */
    public static void exampleDownloadFlow() {
        long trackId = 337502043;
        String quality = "LOSSLESS";
        
        // 一步获取 ManifestResult（推荐方式）
        log.info("========== 获取 Manifest 结果 ==========");
        ManifestResult manifestResult = TidalProxyApiUtils.getDownloadUrlResult(trackId, quality);
        
        if (manifestResult == null) {
            log.error("获取失败");
            return;
        }
        
        // 根据格式处理
        if (manifestResult.isBtsFormat()) {
            // BTS 格式或直接 URL：直接下载
            log.info("✅ BTS/直接 URL 格式");
            log.info("  URL: {}", manifestResult.getDirectUrl());
            log.info("  Codecs: {}", manifestResult.getCodecs());
            
            // 确定输出文件扩展名
            String outputExt = "m4a"; // 默认 m4a
            String codecs = manifestResult.getCodecs();
            if (codecs != null && codecs.toLowerCase().contains("flac")) {
                outputExt = "flac";
            }
            
            String outputPath = "tidal_track_" + trackId + "." + outputExt;
            log.info("  开始下载: {}", outputPath);
            
            boolean success = TidalProxyApiUtils.downloadDirectUrl(manifestResult.getDirectUrl(), outputPath);
            
            if (success) {
                log.info("✓ 直接 URL 下载成功: {}", outputPath);
            } else {
                log.error("✗ 直接 URL 下载失败");
            }
            
        } else if (manifestResult.isDashFormat()) {
            // DASH 格式：分段下载并合并
            log.warn("⚠ DASH 格式，需要分段下载");
            log.info("  初始化段: {}", manifestResult.getInitUrl());
            log.info("  媒体段数量: {}", manifestResult.getSegmentCount());
            log.info("  Codecs: {}", manifestResult.getCodecs());
            
            // 测试 1: 输出为 M4A 格式
            log.info("\n========== 测试 1: 输出 M4A 格式 ==========");
            String outputPathM4a = "tidal_track_" + trackId + ".m4a";
            boolean successM4a = TidalProxyApiUtils.downloadDashSegments(manifestResult, outputPathM4a);
            
            if (successM4a) {
                log.info("✓ M4A 下载成功: {}", outputPathM4a);
            } else {
                log.error("✗ M4A 下载失败");
            }
            
            // 测试 2: 输出为 FLAC 格式
            log.info("\n========== 测试 2: 输出 FLAC 格式 ==========");
            String outputPathFlac = "tidal_track_" + trackId + ".flac";
            boolean successFlac = TidalProxyApiUtils.downloadDashSegments(manifestResult, outputPathFlac);
            
            if (successFlac) {
                log.info("✓ FLAC 下载成功: {}", outputPathFlac);
            } else {
                log.error("✗ FLAC 下载失败");
            }
        } else {
            log.error("❌ 无法识别的格式");
        }
    }
    
    /**
     * 主函数 - 运行示例
     */
    public static void main(String[] args) {
        log.info("Tidal Proxy API Utils 测试");
        log.info("==========================");
        
        // 运行示例
        exampleDownloadFlow();
    }
}
