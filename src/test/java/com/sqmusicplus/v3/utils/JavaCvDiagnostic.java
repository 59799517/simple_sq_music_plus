//package com.sqmusicplus.v3.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//
//import java.io.File;
//
///**
// * @Classname JavaCvDiagnostic
// * @Description JavaCV FFmpeg 诊断工具
// * @Version 1.0.0
// * @Date 2026/5/8
// * @Created by SQ
// */
//@Slf4j
//public class JavaCvDiagnostic {
//
//    public static void main(String[] args) {
//        log.info("========== JavaCV FFmpeg 诊断工具 ==========\n");
//
//        // 1. 检查 JavaCV 版本
//        checkJavaCvVersion();
//
//        // 2. 检查 FFmpeg 路径
//        checkFfmpegPath();
//
//        // 3. 检查 FFmpeg 可执行文件
//        checkFfmpegExecutable();
//
//        // 4. 测试 FFmpeg 功能
//        testFfmpegFunctionality();
//
//        log.info("\n========== 诊断完成 ==========");
//    }
//
//    /**
//     * 检查 JavaCV 版本
//     */
//    private static void checkJavaCvVersion() {
//        log.info("【步骤1】检查 JavaCV 版本");
//        try {
//            Package pkg = FFmpegFrameGrabber.class.getPackage();
//            if (pkg != null) {
//                log.info("  JavaCV 版本: {}", pkg.getImplementationVersion());
//                log.info("  JavaCV 标题: {}", pkg.getImplementationTitle());
//                log.info("  JavaCV 供应商: {}", pkg.getImplementationVendor());
//            } else {
//                log.warn("  ⚠ 无法获取 JavaCV 包信息");
//            }
//
//            // 检查类路径
//            String classPath = FFmpegFrameGrabber.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//            log.info("  JavaCV JAR 位置: {}", classPath);
//            log.info("  ✓ JavaCV 库已加载\n");
//
//        } catch (Exception e) {
//            log.error("  ✗ JavaCV 检查失败: {}", e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 检查 FFmpeg 路径
//     */
//    private static void checkFfmpegPath() {
//        log.info("【步骤2】检查 FFmpeg 路径");
//        try {
//            String ffmpegPath = FFmpegFrameGrabber.getFFmpegPath();
//
//            if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
//                log.info("  ✓ FFmpeg 路径: {}", ffmpegPath);
//
//                File ffmpegFile = new File(ffmpegPath);
//                if (ffmpegFile.exists()) {
//                    log.info("  ✓ FFmpeg 文件存在");
//                    log.info("  文件大小: {} bytes ({})",
//                        ffmpegFile.length(),
//                        formatFileSize(ffmpegFile.length()));
//                    log.info("  是否可执行: {}", ffmpegFile.canExecute());
//                    log.info("  绝对路径: {}", ffmpegFile.getAbsolutePath());
//                } else {
//                    log.error("  ✗ FFmpeg 文件不存在！");
//                    log.error("  这是问题的根本原因");
//                    log.error("  可能的原因:");
//                    log.error("    1. javacv-platform 依赖未正确下载");
//                    log.error("    2. FFmpeg 二进制文件解压失败");
//                    log.error("    3. 临时目录权限问题");
//                }
//            } else {
//                log.error("  ✗ 无法获取 FFmpeg 路径");
//                log.error("  可能的原因:");
//                log.error("    1. javacv-platform 依赖未添加");
//                log.error("    2. JavaCV 初始化失败");
//            }
//
//        } catch (Exception e) {
//            log.error("  ✗ 检查 FFmpeg 路径失败: {}", e.getMessage(), e);
//        }
//        log.info("");
//    }
//
//    /**
//     * 检查 FFmpeg 可执行文件
//     */
//    private static void checkFfmpegExecutable() {
//        log.info("【步骤3】检查 FFmpeg 可执行文件");
//        try {
//            String ffmpegPath = FFmpegFrameGrabber.getFFmpegPath();
//
//            if (ffmpegPath == null || ffmpegPath.isEmpty()) {
//                log.error("  ✗ FFmpeg 路径为空");
//                return;
//            }
//
//            File ffmpegFile = new File(ffmpegPath);
//            if (!ffmpegFile.exists()) {
//                log.error("  ✗ FFmpeg 文件不存在: {}", ffmpegPath);
//                return;
//            }
//
//            log.info("  ✓ FFmpeg 文件存在");
//            log.info("  文件类型: {}", ffmpegFile.isFile() ? "文件" : "目录");
//            log.info("  文件大小: {} bytes", ffmpegFile.length());
//            log.info("  可读: {}", ffmpegFile.canRead());
//            log.info("  可执行: {}", ffmpegFile.canExecute());
//
//        } catch (Exception e) {
//            log.error("  ✗ 检查 FFmpeg 可执行文件失败: {}", e.getMessage(), e);
//        }
//        log.info("");
//    }
//
//    /**
//     * 测试 FFmpeg 功能
//     */
//    private static void testFfmpegFunctionality() {
//        log.info("【步骤4】测试 FFmpeg 版本");
//        try {
//            String ffmpegPath = FFmpegFrameGrabber.getFFmpegPath();
//
//            if (ffmpegPath == null || ffmpegPath.isEmpty()) {
//                log.error("  ✗ FFmpeg 路径为空");
//                return;
//            }
//
//            // 执行 FFmpeg -version
//            ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-version");
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            // 读取输出（只读前10行）
//            java.io.BufferedReader reader = new java.io.BufferedReader(
//                new java.io.InputStreamReader(process.getInputStream()));
//
//            int lineCount = 0;
//            StringBuilder output = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null && lineCount < 10) {
//                output.append("    ").append(line).append("\n");
//                lineCount++;
//            }
//            reader.close();
//
//            int exitCode = process.waitFor();
//
//            if (exitCode == 0) {
//                log.info("  ✓ FFmpeg 执行成功");
//                log.info("  版本信息:\n{}", output.toString());
//            } else {
//                log.error("  ✗ FFmpeg 执行失败，退出代码: {}", exitCode);
//                log.error("  输出:\n{}", output.toString());
//            }
//
//        } catch (Exception e) {
//            log.error("  ✗ 执行 FFmpeg 失败: {}", e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 格式化文件大小
//     */
//    private static String formatFileSize(long bytes) {
//        if (bytes < 1024) return bytes + " B";
//        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
//        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
//        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
//    }
//}
