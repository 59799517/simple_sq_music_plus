package com.sqmusicplus.v3.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname FindFfmpeg
 * @Description 查找系统中的 FFmpeg
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class FindFfmpeg {

    public static void main(String[] args) {
        log.info("========== 查找 FFmpeg ==========\n");
        
        // 1. 检查常见安装位置
        List<String> commonPaths = Arrays.asList(
            "C:\\ffmpeg\\bin\\ffmpeg.exe",
            "C:\\Program Files\\ffmpeg\\bin\\ffmpeg.exe",
            "C:\\Program Files (x86)\\ffmpeg\\bin\\ffmpeg.exe",
            "D:\\ffmpeg\\bin\\ffmpeg.exe",
            "C:\\tools\\ffmpeg\\bin\\ffmpeg.exe"
        );
        
        log.info("【步骤1】检查常见安装位置:");
        for (String path : commonPaths) {
            File ffmpeg = new File(path);
            if (ffmpeg.exists()) {
                log.info("  ✓ 找到 FFmpeg: {}", path);
                log.info("  文件大小: {} bytes", ffmpeg.length());
                
                // 测试是否可用
                try {
                    Process process = Runtime.getRuntime().exec("\"" + path + "\" -version");
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        log.info("  ✓ FFmpeg 可用\n");
                        log.info("建议: 将 {} 添加到系统 PATH", new File(path).getParent());
                        return;
                    }
                } catch (Exception e) {
                    log.error("  ✗ 执行失败: {}", e.getMessage());
                }
            }
        }
        log.info("  ✗ 未在常见位置找到 FFmpeg\n");
        
        // 2. 检查 PATH 环境变量
        log.info("【步骤2】检查 PATH 环境变量:");
        String pathEnv = System.getenv("PATH");
        if (pathEnv != null) {
            String[] paths = pathEnv.split(";");
            boolean found = false;
            for (String p : paths) {
                File dir = new File(p);
                if (dir.isDirectory()) {
                    File ffmpegExe = new File(dir, "ffmpeg.exe");
                    if (ffmpegExe.exists()) {
                        log.info("  ✓ 在 PATH 中找到 FFmpeg: {}", ffmpegExe.getAbsolutePath());
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                log.info("  ✗ PATH 中未找到 FFmpeg\n");
            }
        }
        
        // 3. 检查 JAVE 内置的 FFmpeg
        log.info("【步骤3】检查 JAVE 内置 FFmpeg:");
        try {
            Class<?> locatorClass = Class.forName("ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator");
            Object locator = locatorClass.getDeclaredConstructor().newInstance();
            
            // 获取 FFmpeg 路径
            java.lang.reflect.Method getFFMPEGPathMethod = locatorClass.getMethod("getFFMPEGPath");
            String ffmpegPath = (String) getFFMPEGPathMethod.invoke(locator);
            
            if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
                log.info("  ✓ JAVE 内置 FFmpeg: {}", ffmpegPath);
                File ffmpegFile = new File(ffmpegPath);
                if (ffmpegFile.exists()) {
                    log.info("  ✓ 文件存在");
                    log.info("  文件大小: {} bytes", ffmpegFile.length());
                    log.info("\n提示: JAVE 会使用这个内置的 FFmpeg 进行转码");
                    log.info("如果转码失败，可能是内置 FFmpeg 版本过旧或有 bug");
                } else {
                    log.error("  ✗ 文件不存在");
                }
            } else {
                log.info("  ✗ 无法获取 JAVE FFmpeg 路径");
            }
        } catch (Exception e) {
            log.error("  ✗ 检查 JAVE FFmpeg 失败: {}", e.getMessage());
        }
        
        log.info("\n========== 查找完成 ==========");
        log.info("\n建议:");
        log.info("1. 如果已安装 FFmpeg，请将其 bin 目录添加到系统 PATH");
        log.info("2. 或者使用 TranscodingUtils.transcodingM4aToFlacWithSystemFfmpeg() 时指定完整路径");
        log.info("3. 或者继续使用 JAVE 内置 FFmpeg（TranscodingUtils.transcodingM4aToFlac()）");
    }
}
