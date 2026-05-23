package com.sqmusicplus.v3.utils;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @Classname JaveDiagnostic
 * @Description JAVE 和 FFmpeg 诊断工具
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class JaveDiagnostic {

    public static void main(String[] args) {
        log.info("========== JAVE & FFmpeg 诊断工具 ==========\n");
        
        // 1. 检查 JAVE 版本
        checkJaveVersion();
        
        // 2. 检查 FFmpeg Locator
        checkFfmpegLocator();
        
        // 3. 检查 FFmpeg 可执行文件
        checkFfmpegExecutable();
        
        // 4. 测试 FFmpeg 功能
        testFfmpegFunctionality();
        
        // 5. 检查源文件
        File sourceFile = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        if (sourceFile.exists()) {
            inspectSourceFile(sourceFile);
        } else {
            log.warn("⚠ 源文件不存在: {}", sourceFile.getAbsolutePath());
        }
        
        log.info("\n========== 诊断完成 ==========");
    }

    /**
     * 检查 JAVE 版本
     */
    private static void checkJaveVersion() {
        log.info("【步骤1】检查 JAVE 版本");
        try {
            Package pkg = Encoder.class.getPackage();
            if (pkg != null) {
                log.info("  JAVE 版本: {}", pkg.getImplementationVersion());
                log.info("  JAVE 标题: {}", pkg.getImplementationTitle());
                log.info("  JAVE 供应商: {}", pkg.getImplementationVendor());
            } else {
                log.warn("  ⚠ 无法获取 JAVE 包信息");
            }
            
            // 检查类路径
            String classPath = Encoder.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            log.info("  JAVE JAR 位置: {}", classPath);
            log.info("  ✓ JAVE 库已加载\n");
            
        } catch (Exception e) {
            log.error("  ✗ JAVE 检查失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 检查 FFmpeg Locator
     */
    private static void checkFfmpegLocator() {
        log.info("【步骤2】检查 FFmpeg Locator");
        try {
            DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
            log.info("  Locator 类: {}", locator.getClass().getName());
            log.info("  ✓ FFmpeg Locator 创建成功\n");
            
        } catch (Exception e) {
            log.error("  ✗ FFmpeg Locator 创建失败: {}", e.getMessage(), e);
            log.error("  提示: 可能 jave-core 或 jave-all-deps 依赖有问题\n");
        }
    }

    /**
     * 检查 FFmpeg 可执行文件
     */
    private static void checkFfmpegExecutable() {
        log.info("【步骤3】检查 FFmpeg 可执行文件");
        try {
            DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
            
            // 使用反射获取 FFmpeg 路径
            Method getFFMPEGPathMethod = locator.getClass().getMethod("getFFMPEGPath");
            String ffmpegPath = (String) getFFMPEGPathMethod.invoke(locator);
            
            if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
                log.info("  FFmpeg 路径: {}", ffmpegPath);
                
                File ffmpegFile = new File(ffmpegPath);
                if (ffmpegFile.exists()) {
                    log.info("  ✓ FFmpeg 文件存在");
                    log.info("  文件大小: {} bytes ({})", 
                        ffmpegFile.length(),
                        formatFileSize(ffmpegFile.length()));
                    log.info("  是否可执行: {}", ffmpegFile.canExecute());
                    log.info("  绝对路径: {}", ffmpegFile.getAbsolutePath());
                    
                    // 尝试执行 FFmpeg -version
                    testFfmpegVersion(ffmpegPath);
                } else {
                    log.error("  ✗ FFmpeg 文件不存在！");
                    log.error("  这是问题的根本原因");
                    log.error("  可能的原因:");
                    log.error("    1. jave-all-deps.jar 未正确下载");
                    log.error("    2. FFmpeg 二进制文件解压失败");
                    log.error("    3. 临时目录权限问题");
                }
            } else {
                log.error("  ✗ 无法获取 FFmpeg 路径");
            }
            
        } catch (Exception e) {
            log.error("  ✗ 检查 FFmpeg 失败: {}", e.getMessage(), e);
        }
        log.info("");
    }

    /**
     * 测试 FFmpeg 版本
     */
    private static void testFfmpegVersion(String ffmpegPath) {
        log.info("  【测试】执行 FFmpeg -version");
        try {
            ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-version");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // 读取输出（只读前5行）
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));
            
            int lineCount = 0;
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null && lineCount < 5) {
                output.append("    ").append(line).append("\n");
                lineCount++;
            }
            reader.close();
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                log.info("  ✓ FFmpeg 执行成功");
                log.info("  版本信息:\n{}", output.toString());
            } else {
                log.error("  ✗ FFmpeg 执行失败，退出代码: {}", exitCode);
                log.error("  输出:\n{}", output.toString());
            }
            
        } catch (Exception e) {
            log.error("  ✗ 执行 FFmpeg 失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 测试 FFmpeg 功能
     */
    private static void testFfmpegFunctionality() {
        log.info("【步骤4】测试 FFmpeg 转码功能");
        try {
            Encoder encoder = new Encoder();
            log.info("  ✓ Encoder 创建成功");
            log.info("  Encoder 类: {}", encoder.getClass().getName());
            
            // 检查是否有 native 库
            log.info("  提示: JAVE 会在首次使用时解压 FFmpeg 到临时目录");
            log.info("  临时目录: {}", System.getProperty("java.io.tmpdir"));
            
        } catch (Exception e) {
            log.error("  ✗ Encoder 创建失败: {}", e.getMessage(), e);
        }
        log.info("");
    }

    /**
     * 检查源文件
     */
    private static void inspectSourceFile(File file) {
        log.info("【步骤5】检查源文件");
        log.info("  文件名: {}", file.getName());
        log.info("  文件大小: {} bytes ({})", file.length(), formatFileSize(file.length()));
        log.info("  文件存在: {}", file.exists());
        log.info("  可读: {}", file.canRead());
        
        try {
            MultimediaObject multimediaObject = new MultimediaObject(file);
            MultimediaInfo info = multimediaObject.getInfo();
            
            log.info("  ✓ 文件信息读取成功");
            log.info("  格式: {}", info.getFormat());
            log.info("  时长: {} ms", info.getDuration());
            
            if (info.getAudio() != null) {
                log.info("  音频编码: {}", info.getAudio().getDecoder());
                log.info("  采样率: {} Hz", info.getAudio().getSamplingRate());
                log.info("  声道数: {}", info.getAudio().getChannels());
                log.info("  比特率: {} bps", info.getAudio().getBitRate());
            }
            
        } catch (Exception e) {
            log.error("  ✗ 读取文件信息失败: {}", e.getMessage(), e);
            log.error("  可能原因:");
            log.error("    1. 文件格式不支持");
            log.error("    2. 文件损坏");
            log.error("    3. FFmpeg 解码器问题");
        }
    }

    /**
     * 格式化文件大小
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
