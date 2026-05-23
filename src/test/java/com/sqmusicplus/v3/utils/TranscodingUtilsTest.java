package com.sqmusicplus.v3.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Classname TranscodingUtilsTest
 * @Description 转码工具类测试示例
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class TranscodingUtilsTest {

    public static void main(String[] args) {
        // 方式1: 使用 JavaCV FFmpeg（推荐，包含所有编解码器）⭐⭐⭐⭐⭐
        testM4aToFlacWithJavaCv();
        
        // 方式2: 使用 JAVE API（可能缺少编解码器）
        // testM4aToFlac();
    }

    /**
     * 测试 M4A 转 FLAC（使用 JAVE 内置 FFmpeg）
     */
    public static void testM4aToFlacWithJaveFfmpeg() {
        log.info("========== 测试 M4A 转 FLAC（JAVE 内置 FFmpeg） ==========");
        
        File source = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        File target = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043_jave.flac");
        
        boolean success = TranscodingUtils.transcodingM4aToFlacWithJaveFfmpeg(source, target);
        
        if (success) {
            log.info("✓ 转码成功: {}", target.getAbsolutePath());
            log.info("文件大小: {} bytes", target.length());
        } else {
            log.error("✗ 转码失败");
        }
    }

    /**
     * 测试 M4A 转 FLAC（使用系统 FFmpeg）
     */
    public static void testM4aToFlacWithSystemFfmpeg() {
        log.info("========== 测试 M4A 转 FLAC（系统 FFmpeg） ==========");
        
        File source = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        File target = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043_system_ffmpeg.flac");
        
        boolean success = TranscodingUtils.transcodingM4aToFlacWithSystemFfmpeg(source, target);
        
        if (success) {
            log.info("✓ 转码成功: {}", target.getAbsolutePath());
            log.info("文件大小: {} bytes", target.length());
        } else {
            log.error("✗ 转码失败");
            log.error("提示: 请确保 FFmpeg 已安装并添加到系统 PATH");
        }
    }

    /**
     * 测试 M4A 转 FLAC（使用 JavaCV）
     */
    public static void testM4aToFlacWithJavaCv() {
        log.info("========== 测试 M4A 转 FLAC（JavaCV FFmpeg） ==========");
        
        File source = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        File target = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_javacv.flac");
        
        boolean success = com.sqmusicplus.v3.utils.JavacvTranscodingUtils.transcodingM4aToFlac(source, target);
        
        if (success) {
            log.info("✓ 转码成功: {}", target.getAbsolutePath());
            log.info("文件大小: {} bytes", target.length());
        } else {
            log.error("✗ 转码失败");
        }
    }

    /**
     * 测试 M4A 转 FLAC
     */
    public static void testM4aToFlac() {
        log.info("========== 测试 M4A 转 FLAC ==========");
        
        // 源文件（M4A）
        File source = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        
        // 目标文件（FLAC）
        File target = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.flac");
        
        // 执行转码（默认采样率 44100Hz）
        boolean success = TranscodingUtils.transcodingM4aToFlac(source, target);
        
        if (success) {
            log.info("✓ 转码成功: {}", target.getAbsolutePath());
            log.info("文件大小: {} bytes", target.length());
        } else {
            log.error("✗ 转码失败");
        }
    }

    /**
     * 测试 M4A 转 FLAC（自定义采样率）
     */
    public static void testM4aToFlacWithCustomSampleRate() {
        log.info("========== 测试 M4A 转 FLAC（96kHz） ==========");
        
        File source = new File("D:/music/test.m4a");
        File target = new File("D:/music/test_96k.flac");
        
        // 使用 96kHz 采样率（Hi-Res 音质）
        boolean success = TranscodingUtils.transcodingM4aToFlac(source, target, 96000);
        
        if (success) {
            log.info("✓ 转码成功: {}", target.getAbsolutePath());
        } else {
            log.error("✗ 转码失败");
        }
    }

    /**
     * 测试 M4A 转 MP3
     */
    public static void testM4aToMp3() {
        log.info("========== 测试 M4A 转 MP3 ==========");
        
        File source = new File("D:/music/test.m4a");
        File target = new File("D:/music/test.mp3");
        
        // 320kbps 高质量 MP3
        boolean success = TranscodingUtils.transcodingM4aToMp3(source, target, 320000);
        
        if (success) {
            log.info("✓ 转码成功: {}", target.getAbsolutePath());
            log.info("比特率: 320kbps");
        } else {
            log.error("✗ 转码失败");
        }
    }

    /**
     * 批量转码示例
     */
    public static void testBatchTranscoding() {
        log.info("========== 批量转码示例 ==========");
        
        File sourceDir = new File("D:/music/m4a");
        File targetDir = new File("D:/music/flac");
        
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        
        // 遍历目录下所有 .m4a 文件
        File[] m4aFiles = sourceDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".m4a"));
        
        if (m4aFiles == null || m4aFiles.length == 0) {
            log.warn("未找到 M4A 文件");
            return;
        }
        
        int successCount = 0;
        int failCount = 0;
        
        for (File m4aFile : m4aFiles) {
            String fileName = m4aFile.getName();
            String flacFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".flac";
            File flacFile = new File(targetDir, flacFileName);
            
            log.info("正在转码: {} -> {}", fileName, flacFileName);
            
            if (TranscodingUtils.transcodingM4aToFlac(m4aFile, flacFile)) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        log.info("========== 批量转码完成 ==========");
        log.info("成功: {} 个", successCount);
        log.info("失败: {} 个", failCount);
    }
}
