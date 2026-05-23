package com.sqmusicplus.v3.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @Classname FfmpegDiagnostic
 * @Description FFmpeg 诊断工具
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class FfmpegDiagnostic {

    public static void main(String[] args) {
        log.info("========== FFmpeg 诊断工具 ==========\n");
        
        // 1. 检查 FFmpeg 版本
        checkFfmpegVersion();
        
        // 2. 检查源文件信息
        File sourceFile = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        if (sourceFile.exists()) {
            inspectMediaFile(sourceFile);
            
            // 3. 尝试手动转码
            testManualTranscoding(sourceFile);
        } else {
            log.error("源文件不存在: {}", sourceFile.getAbsolutePath());
        }
    }

    /**
     * 检查 FFmpeg 版本
     */
    private static void checkFfmpegVersion() {
        log.info("【步骤1】检查 FFmpeg 版本");
        try {
            Process process = Runtime.getRuntime().exec("ffmpeg -version");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null && lineCount < 3) {
                log.info("  {}", line);
                lineCount++;
            }
            
            reader.close();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                log.info("✓ FFmpeg 可用\n");
            } else {
                log.error("✗ FFmpeg 不可用，退出代码: {}\n", exitCode);
            }
            
        } catch (Exception e) {
            log.error("✗ 无法执行 FFmpeg: {}", e.getMessage());
            log.error("提示: 请确保 FFmpeg 已安装并添加到系统 PATH\n");
        }
    }

    /**
     * 检查媒体文件信息
     */
    private static void inspectMediaFile(File file) {
        log.info("【步骤2】检查源文件信息");
        log.info("  文件名: {}", file.getName());
        log.info("  文件大小: {} bytes", file.length());
        log.info("  文件存在: {}", file.exists());
        
        try {
            Process process = Runtime.getRuntime().exec("ffprobe -v quiet -print_format json -show_format -show_streams \"" + file.getAbsolutePath() + "\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            reader.close();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                log.info("✓ 文件信息获取成功");
                // 解析关键信息
                String json = output.toString();
                if (json.contains("\"codec_name\"")) {
                    int codecStart = json.indexOf("\"codec_name\"") + 14;
                    int codecEnd = json.indexOf("\"", codecStart);
                    String codec = json.substring(codecStart, codecEnd);
                    log.info("  编码格式: {}", codec);
                }
                if (json.contains("\"sample_rate\"")) {
                    int rateStart = json.indexOf("\"sample_rate\"") + 15;
                    int rateEnd = json.indexOf("\"", rateStart);
                    String sampleRate = json.substring(rateStart, rateEnd);
                    log.info("  采样率: {} Hz", sampleRate);
                }
                if (json.contains("\"channels\"")) {
                    int chStart = json.indexOf("\"channels\"") + 12;
                    int chEnd = json.indexOf(",", chStart);
                    String channels = json.substring(chStart, chEnd).trim();
                    log.info("  声道数: {}", channels);
                }
            } else {
                log.error("✗ 无法获取文件信息，退出代码: {}", exitCode);
            }
            
        } catch (Exception e) {
            log.error("✗ 检查文件失败: {}", e.getMessage(), e);
        }
        log.info("");
    }

    /**
     * 测试手动转码
     */
    private static void testManualTranscoding(File sourceFile) {
        log.info("【步骤3】测试手动 FFmpeg 转码");
        
        File targetFile = new File(sourceFile.getParent(), "test_manual.flac");
        String command = String.format("ffmpeg -i \"%s\" -acodec flac -ar 44100 -ac 2 \"%s\"", 
            sourceFile.getAbsolutePath(), 
            targetFile.getAbsolutePath()
        );
        
        log.info("  执行命令:");
        log.info("  {}", command);
        log.info("");
        
        try {
            Process process = Runtime.getRuntime().exec(command);
            
            // 读取标准输出
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = stdoutReader.readLine()) != null) {
                log.info("  [OUT] {}", line);
            }
            
            // 读取错误输出（FFmpeg 的输出通常在 stderr）
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = stderrReader.readLine()) != null) {
                log.info("  [ERR] {}", line);
            }
            
            stdoutReader.close();
            stderrReader.close();
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                log.info("✓ 手动转码成功");
                log.info("  输出文件: {}", targetFile.getAbsolutePath());
                log.info("  文件大小: {} bytes", targetFile.length());
                
                // 清理测试文件
                if (targetFile.exists()) {
                    targetFile.delete();
                    log.info("  已删除测试文件");
                }
            } else {
                log.error("✗ 手动转码失败，退出代码: {}", exitCode);
                log.error("  这说明 FFmpeg 本身有问题，不是 JAVE 库的问题");
            }
            
        } catch (Exception e) {
            log.error("✗ 执行转码命令失败: {}", e.getMessage(), e);
        }
        
        log.info("\n========== 诊断完成 ==========");
    }
}
